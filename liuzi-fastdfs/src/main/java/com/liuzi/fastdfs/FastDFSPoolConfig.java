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
    private static TrackerServer trackerServer;
    private static TrackerClient trackerClient;
    private static StorageServer storageServer;
    private static StorageClient storageClient;
    
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
    	StorageClient storageClient1 = null;
        try {
        	storageClient1 = idleConnectionPool.poll(waitTime, TimeUnit.SECONDS);
        	//storageClient1 = idleConnectionPool.take();
        	logger.info("获取连接，当前剩余空闲连接：" + idleConnectionPool.size());
            if(storageClient1 != null){
            	trackerClient = new TrackerClient();
        		trackerServer = trackerClient.getConnection();
        		storageClient1 = new StorageClient(trackerServer, storageServer);
        		
            	busyConnectionPool.put(storageClient1, obj);
            	logger.info("busyConnPool增加，当前使用连接：" + busyConnectionPool.size());
            	return storageClient1;
            }
            /*trackerServer = trackerClient.getConnection();
            storageClient1 = new StorageClient(trackerServer, storageServer);
            idleConnectionPool.add(storageClient1);*/
        } catch (Exception e) {
        	storageClient1 = null;
            logger.error("busyConnectionPool checkOut fail：" + e.getMessage());
            e.printStackTrace();
        }
        return storageClient1;
    }
    
    //回收连接
    public static void checkIn(StorageClient storageClient1) {
    	Object obj = busyConnectionPool.remove(storageClient1);
    	logger.info("busyConnPool移除，剩余连接：" + busyConnectionPool.size());
        if(obj != null){
        	/*TrackerServer trackerServer = null;
        	try {
        		TrackerClient trackerClient = new TrackerClient();
        		trackerServer = trackerClient.getConnection();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
	            if(trackerServer != null){
	                try {
	                    trackerServer.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                    logger.error("checkIn trackerServer close 失败：" + e.getMessage());
	                }
	            }
	        }*/
        	storageClient1 = new StorageClient(trackerServer, storageServer);
        	idleConnectionPool.add(storageClient1);
        	logger.info("idleConnPool回收，剩余连接" + idleConnectionPool.size());
        }
    }
    
    //如果连接无效则抛弃，新建连接来补充到池里
    public static void drop(StorageClient storageClient1){
        if(FastDFSPoolConfig.busyConnectionPool.remove(storageClient1) != null){
            TrackerServer trackerServer = null;
            TrackerClient trackerClient = new TrackerClient();
            try {
                trackerServer = trackerClient.getConnection();
                StorageClient newStorageClient = new StorageClient(trackerServer, null);
                idleConnectionPool.add(newStorageClient);
                
                logger.info("------------------------- conn：" + idleConnectionPool.size());
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("busyConnectionPool drop fail：" + e.getMessage());
            }finally{
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
