package com.liuzi.fastdfs;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import lombok.Getter;
import lombok.Setter;

import com.liuzi.fastdfs.base.ClientGlobal;
import com.liuzi.fastdfs.base.StorageClient;
import com.liuzi.fastdfs.base.StorageServer;
import com.liuzi.fastdfs.base.TrackerClient;
import com.liuzi.fastdfs.base.TrackerServer;
import com.liuzi.util.common.LiuziUtil;
import com.liuzi.util.common.Log;


public class FastDFSPoolConfig{
	
	private static final Object OBJECT = new Object();
	
	/**
	 * 配置文件目录
	 */
	@Getter @Setter private String configPath;
	
    private ConcurrentHashMap<StorageClient, Object> busyConnectionPool;//被使用的连接
    private ArrayBlockingQueue<StorageClient> idleConnectionPool;//空闲的连接
    
    public void init(){
    	LiuziUtil.tag("  --------  Liuzi FastDFS Pool初始化......  --------", "v1.1.20");
    	
    	Log.info("===== fastdfs初始化连接池...... ========");
    	
    	TrackerServer trackerServer = null;
    	StorageServer storageServer = null;
    	
    	TrackerClient trackerClient;
        StorageClient storageClient;
    	
        try {
        	ClientGlobal.init(configPath);
        	
        	int size = ClientGlobal.g_connection_pool_size;
        	busyConnectionPool = new ConcurrentHashMap<StorageClient, Object>();
            idleConnectionPool = new ArrayBlockingQueue<StorageClient>(size);
        	
            trackerClient = new TrackerClient();
            trackerServer = trackerClient.getConnection();
            
            for(int i = 0; i < size; i++){
                storageClient = new StorageClient(trackerServer, storageServer);
                idleConnectionPool.add(storageClient);
            }
            Log.info("idleConnectionPool.size：{}", idleConnectionPool.size());
            
        } catch (Exception e) {
        	Log.error(e, "初始化连接池失败");
        } finally {
            if(storageServer != null){
                try {
                	storageServer.close();
                } catch (IOException e) {
                    Log.error(e, "storageServer close 失败");
                }
            }
            if(trackerServer != null){
                try {
                    trackerServer.close();
                    
                } catch (IOException e) {
                    Log.error(e, "trackerServer close 失败");
                }
            }
            
            Log.info("===== fastdfs初始化连接池完成...... ========\n");
        }
    }
    
    //取出连接
    public StorageClient checkOut(int waitTime){
    	TrackerServer trackerServer = null;
    	StorageServer storageServer = null;
    	
    	//TrackerClient trackerClient;
        StorageClient storageClient;
    	
        try {
        	storageClient = idleConnectionPool.poll(waitTime, TimeUnit.SECONDS);
        	//Log.info("获取连接，当前剩余空闲连接：" + idleConnectionPool.size());
            if(storageClient != null){
            	/*trackerClient = new TrackerClient();
        		trackerServer = trackerClient.getConnection();
        		storageClient = new StorageClient(trackerServer, storageServer);*/
        		
            	busyConnectionPool.put(storageClient, OBJECT);
            	//log.info("busyConnPool增加，当前使用连接：" + busyConnectionPool.size());
            	return storageClient;
            }
        } catch (Exception e) {
        	storageClient = null;
            Log.error(e, "busyConnectionPool checkOut error");
        } finally {
			if(storageServer != null){
                try {
                	storageServer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.error(e, "storageServer close error");
                }
            }
            if(trackerServer != null){
                try {
                    trackerServer.close();
                } catch (IOException e) {
                    Log.error(e, "checkIn trackerServer close error");
                }
            }
        }
        return storageClient;
    }
    
    //回收连接
    public void checkIn(StorageClient storageClient) {
    	Object obj = busyConnectionPool.remove(storageClient);
    	//Log.info("busyConnPool移除，剩余连接：" + busyConnectionPool.size());
        if(obj != null){
        	TrackerServer trackerServer = null;
        	TrackerClient trackerClient = null;
        	StorageServer storageServer = null;
        	
        	try {
        		trackerClient = new TrackerClient();
        		trackerServer = trackerClient.getConnection();
        		storageClient = new StorageClient(trackerServer, storageServer);
            	idleConnectionPool.add(storageClient);
            	//Log.info("idleConnPool回收，剩余连接" + idleConnectionPool.size());
			} catch (IOException e) {
				Log.error(e, "busyConnPool回收错误");
			} finally {
				if(storageServer != null){
	                try {
	                	storageServer.close();
	                } catch (IOException e) {
	                    Log.error(e, "storageServer close 失败");
	                }
	            }
	            if(trackerServer != null){
	                try {
	                    trackerServer.close();
	                } catch (IOException e) {
	                    Log.error(e, "checkIn trackerServer close 失败");
	                }
	            }
	        }
        }
    }
    
    //如果连接无效则抛弃，新建连接来补充到池里
    public void drop(StorageClient storageClient){
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
                //log.info("------------------------- conn：" + idleConnectionPool.size());
            } catch (IOException e) {
            	Log.error(e, "storageServer close fail");
                //log.error("busyConnectionPool drop fail：" + e.getMessage());
            }finally{
            	if(storageServer != null){
                    try {
                    	storageServer.close();
                    } catch (IOException e) {
                    	Log.error(e, "storageServer close fail");
                    }
                }
                if(trackerServer != null){
                    try {
                        trackerServer.close();
                    } catch (IOException e) {
                    	Log.error(e, "trackerServer close fail");
                    }
                }
            }
        }
    }
}
