package com.liuzi.fastdfs.boot;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

import com.liuzi.fastdfs.boot.base.ClientGlobal;
import com.liuzi.fastdfs.boot.base.StorageClient;
import com.liuzi.fastdfs.boot.base.StorageServer;
import com.liuzi.fastdfs.boot.base.TrackerClient;
import com.liuzi.fastdfs.boot.base.TrackerServer;
import com.liuzi.util.common.LiuziUtil;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class FastDFSPoolConfig {
	
	private static Object obj = new Object();
    private static ConcurrentHashMap<StorageClient, Object> busyConnectionPool = null;//被使用的连接
    private static ArrayBlockingQueue<StorageClient> idleConnectionPool;//空闲的连接
    
	@Value("${fdfs.connect_timeout}")
	private int g_connect_timeout;
	@Value("${fdfs.network_timeout}")
	public int g_network_timeout;
	@Value("${fdfs.charset}")
	public String g_charset;
	@Value("${fdfs.http.anti_steal_token}")
	public boolean g_anti_steal_token;
	@Value("${fdfs.http.secret_key}")
	public String g_secret_key;
	@Value("${fdfs.http.tracker_http_port}")
	public int g_tracker_http_port;
	@Value("${fdfs.upload.max.size}")
	public int g_file_upload_max_size;
	@Value("${fdfs.connect.pool.size}")
	public int g_connection_pool_size;
	@Value("${fdfs.file_server}")
	public String fileServer;
	@Value("${fdfs.tracker_server}")
	public String trackerServer;
	
	@Bean
    public void init(){
    	LiuziUtil.tag("--------  Liuzi FastDFS Pool初始化  --------");
    	
    	ClientGlobal.g_connect_timeout = g_connect_timeout;
    	ClientGlobal.g_network_timeout = g_network_timeout;
    	ClientGlobal.g_charset = g_charset;
    	ClientGlobal.g_anti_steal_token = g_anti_steal_token;
    	ClientGlobal.g_secret_key = g_secret_key;
    	ClientGlobal.g_tracker_http_port = g_tracker_http_port;
    	ClientGlobal.g_file_upload_max_size = g_file_upload_max_size;
    	ClientGlobal.g_connection_pool_size = g_connection_pool_size;
    	ClientGlobal.fileServer = fileServer;
    	ClientGlobal.trackerServer = trackerServer;
    	
    	TrackerServer trackerServer = null;
    	StorageServer storageServer = null;
    	
    	TrackerClient trackerClient;
        StorageClient storageClient;
    	
        try {
        	ClientGlobal.init();
        	
        	int size = g_connection_pool_size;
        	busyConnectionPool = new ConcurrentHashMap<StorageClient, Object>();
            idleConnectionPool = new ArrayBlockingQueue<StorageClient>(size);
        	
            trackerClient = new TrackerClient();
            trackerServer = trackerClient.getConnection();
            
            for(int i = 0; i < size; i++){
                storageClient = new StorageClient(trackerServer, storageServer);
                idleConnectionPool.add(storageClient);
            }
            log.info("idleConnectionPool.size：" + idleConnectionPool.size());
            
        } catch (Exception e) {
        	log.error("初始化连接池失败：" + e.getMessage());
            e.printStackTrace();
        } finally {
            if(storageServer != null){
                try {
                	storageServer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("storageServer close 失败：" + e.getMessage());
                }
            }
            if(trackerServer != null){
                try {
                    trackerServer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("trackerServer close 失败：" + e.getMessage());
                }
            }
            
            log.info("--------  Liuzi FastDFS Pool初始化完成  --------");
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
        	log.info("获取连接，当前剩余空闲连接：" + idleConnectionPool.size());
            if(storageClient != null){
            	/*trackerClient = new TrackerClient();
        		trackerServer = trackerClient.getConnection();
        		storageClient = new StorageClient(trackerServer, storageServer);*/
        		
            	busyConnectionPool.put(storageClient, obj);
            	log.info("busyConnPool增加，当前使用连接：" + busyConnectionPool.size());
            	return storageClient;
            }
        } catch (Exception e) {
        	storageClient = null;
        	log.error("busyConnectionPool checkOut fail：" + e.getMessage());
            e.printStackTrace();
        } finally {
			if(storageServer != null){
                try {
                	storageServer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("storageServer close 失败：" + e.getMessage());
                }
            }
            if(trackerServer != null){
                try {
                    trackerServer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("checkIn trackerServer close 失败：" + e.getMessage());
                }
            }
        }
        return storageClient;
    }
    
    //回收连接
    public static void checkIn(StorageClient storageClient) {
    	Object obj = busyConnectionPool.remove(storageClient);
    	log.info("busyConnPool移除，剩余连接：" + busyConnectionPool.size());
        if(obj != null){
        	TrackerServer trackerServer = null;
        	TrackerClient trackerClient = null;
        	StorageServer storageServer = null;
        	
        	try {
        		trackerClient = new TrackerClient();
        		trackerServer = trackerClient.getConnection();
        		storageClient = new StorageClient(trackerServer, storageServer);
            	idleConnectionPool.add(storageClient);
            	log.info("idleConnPool回收，剩余连接" + idleConnectionPool.size());
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if(storageServer != null){
	                try {
	                	storageServer.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                    log.error("storageServer close 失败：" + e.getMessage());
	                }
	            }
	            if(trackerServer != null){
	                try {
	                    trackerServer.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                    log.error("checkIn trackerServer close 失败：" + e.getMessage());
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
                
                log.info("------------------------- conn：" + idleConnectionPool.size());
            } catch (IOException e) {
                e.printStackTrace();
                log.error("busyConnectionPool drop fail：" + e.getMessage());
            }finally{
            	if(storageServer != null){
                    try {
                    	storageServer.close();
                    } catch (IOException e) {
                    	log.error("storageServer close fail：" + e.getMessage());
                        e.printStackTrace();
                    }
                }
                if(trackerServer != null){
                    try {
                        trackerServer.close();
                    } catch (IOException e) {
                    	log.error("trackerServer close fail：" + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
