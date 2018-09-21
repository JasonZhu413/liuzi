package com.liuzi.fastdfs;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.liuzi.fastdfs.base.ClientGlobal;
import com.liuzi.fastdfs.base.StorageClient;
import com.liuzi.fastdfs.base.StorageServer;
import com.liuzi.fastdfs.base.TrackerClient;
import com.liuzi.fastdfs.base.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.liuzi.util.LiuziUtil;


public class FastDFSPoolConfig {
	
	private static Logger logger = LoggerFactory.getLogger(FastDFSPoolConfig.class);

	private final static String DEFAULT_CONF_FILE_NAME = "conf/fdfs.properties";
	
	private static String g_conf_file = DEFAULT_CONF_FILE_NAME;
    private static Object obj = new Object();
    
    private static ConcurrentHashMap<StorageClient, Object> busyConnectionPool = null;//被使用的连接
    private static ArrayBlockingQueue<StorageClient> idleConnectionPool;//空闲的连接
    
    public FastDFSPoolConfig(){
        init();
    }
    
    public FastDFSPoolConfig(String fileName){
    	if(!StringUtils.isEmpty(fileName)) g_conf_file = fileName;
        init();
    }
    
    //初始化连接池
    private void init(){
    	LiuziUtil.tag("  --------  Liuzi FastDFS Pool初始化......  --------");
    	
    	logger.info("===== fastdfs初始化连接池...... ========");
    	
    	TrackerServer trackerServer = null;
    	StorageServer storageServer = null;
    	
    	TrackerClient trackerClient;
        StorageClient storageClient;
    	
        try {
        	ClientGlobal.init(g_conf_file);
        	
        	int size = ClientGlobal.g_connection_pool_size;
        	busyConnectionPool = new ConcurrentHashMap<StorageClient, Object>();
            idleConnectionPool = new ArrayBlockingQueue<StorageClient>(size);
        	
            trackerClient = new TrackerClient();
            trackerServer = trackerClient.getConnection();
            
            for(int i = 0; i < size; i++){
                storageClient = new StorageClient(trackerServer, storageServer);
                idleConnectionPool.add(storageClient);
            }
            logger.info("idleConnectionPool.size：" + idleConnectionPool.size());
            
        } catch (Exception e) {
        	logger.error("初始化连接池失败：" + e.getMessage());
            e.printStackTrace();
        } finally {
            if(storageServer != null){
                try {
                	storageServer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error("storageServer close 失败：" + e.getMessage());
                }
            }
            if(trackerServer != null){
                try {
                    trackerServer.close();
                    
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error("trackerServer close 失败：" + e.getMessage());
                }
            }
            
            logger.info("===== fastdfs初始化连接池完成...... ========\n");
        }
    }
    
    //取出连接
    public static StorageClient checkOut(int waitTime){
    	TrackerServer trackerServer = null;
    	StorageServer storageServer = null;
    	
    	//TrackerClient trackerClient;
        StorageClient storageClient;
    	
        try {
        	storageClient = idleConnectionPool.poll(waitTime, TimeUnit.SECONDS);
        	logger.info("获取连接，当前剩余空闲连接：" + idleConnectionPool.size());
            if(storageClient != null){
            	/*trackerClient = new TrackerClient();
        		trackerServer = trackerClient.getConnection();
        		storageClient = new StorageClient(trackerServer, storageServer);*/
        		
            	busyConnectionPool.put(storageClient, obj);
            	logger.info("busyConnPool增加，当前使用连接：" + busyConnectionPool.size());
            	return storageClient;
            }
        } catch (Exception e) {
        	storageClient = null;
            logger.error("busyConnectionPool checkOut fail：" + e.getMessage());
            e.printStackTrace();
        } finally {
			if(storageServer != null){
                try {
                	storageServer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error("storageServer close 失败：" + e.getMessage());
                }
            }
            if(trackerServer != null){
                try {
                    trackerServer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error("checkIn trackerServer close 失败：" + e.getMessage());
                }
            }
        }
        return storageClient;
    }
    
    //回收连接
    public static void checkIn(StorageClient storageClient) {
    	Object obj = busyConnectionPool.remove(storageClient);
    	logger.info("busyConnPool移除，剩余连接：" + busyConnectionPool.size());
        if(obj != null){
        	TrackerServer trackerServer = null;
        	TrackerClient trackerClient = null;
        	StorageServer storageServer = null;
        	
        	try {
        		trackerClient = new TrackerClient();
        		trackerServer = trackerClient.getConnection();
        		storageClient = new StorageClient(trackerServer, storageServer);
            	idleConnectionPool.add(storageClient);
            	logger.info("idleConnPool回收，剩余连接" + idleConnectionPool.size());
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if(storageServer != null){
	                try {
	                	storageServer.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                    logger.error("storageServer close 失败：" + e.getMessage());
	                }
	            }
	            if(trackerServer != null){
	                try {
	                    trackerServer.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                    logger.error("checkIn trackerServer close 失败：" + e.getMessage());
	                }
	            }
	        }
        }
    }
    
    //如果连接无效则抛弃，新建连接来补充到池里
    public static void drop(StorageClient storageClient){
    	Object obj = busyConnectionPool.remove(storageClient);
        if(obj != null){
        	StorageServer storageServer = null;
            TrackerServer trackerServer = null;
            TrackerClient trackerClient;
            
            try {
            	trackerClient = new TrackerClient();
                trackerServer = trackerClient.getConnection();
                storageClient = new StorageClient(trackerServer, storageServer);
                idleConnectionPool.add(storageClient);
                
                logger.info("------------------------- conn：" + idleConnectionPool.size());
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("busyConnectionPool drop fail：" + e.getMessage());
            }finally{
            	if(storageServer != null){
                    try {
                    	storageServer.close();
                    } catch (IOException e) {
                    	logger.error("storageServer close fail：" + e.getMessage());
                        e.printStackTrace();
                    }
                }
                if(trackerServer != null){
                    try {
                        trackerServer.close();
                    } catch (IOException e) {
                    	logger.error("trackerServer close fail：" + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
