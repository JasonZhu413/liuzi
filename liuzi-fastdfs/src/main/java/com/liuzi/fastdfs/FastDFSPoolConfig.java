package com.liuzi.fastdfs;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.liuzi.util.LiuziUtil;


public class FastDFSPoolConfig {
	
	private static Logger logger = LoggerFactory.getLogger(FastDFSPoolConfig.class);

	private final static int MAX_CONNECT_SIZE = 5;//最大连接数,可以写配置文件
	private final static String DEFAULT_CONF_FILE_NAME = "conf/fdfs.properties";
	
	private static String g_conf_file = DEFAULT_CONF_FILE_NAME;
    private static int g_conf_size = MAX_CONNECT_SIZE;
    private static Object obj = new Object();
    
    protected static ConcurrentHashMap<StorageClient, Object> busyConnectionPool = null;//被使用的连接
    protected static ArrayBlockingQueue<StorageClient> idleConnectionPool;//空闲的连接
    private static TrackerServer trackerServer;
    private static TrackerClient trackerClient;
    private static StorageServer storageServer;
    private static StorageClient storageClient1;
    
    public FastDFSPoolConfig(){
        init();
    }
    
    public FastDFSPoolConfig(String fileName){
    	if(!StringUtils.isEmpty(fileName)) g_conf_file = fileName;
        init();
    }
    
    public FastDFSPoolConfig(Integer size){
    	if(size != null && size != 0) g_conf_size = size;
        init();
    }
    
    public FastDFSPoolConfig(String fileName, Integer size){
    	if(size != null && size != 0) g_conf_size = size;
    	if(!StringUtils.isEmpty(fileName)) g_conf_file = fileName;
    		
        init();
    }
    
    //初始化连接池
    protected static void init(){
    	LiuziUtil.tag("Liuzi FastDFS Pool初始化......");
    	
    	logger.info("===== fastdfs初始化连接池...... ========");
    	
    	busyConnectionPool = new ConcurrentHashMap<StorageClient, Object>();
        idleConnectionPool = new ArrayBlockingQueue<StorageClient>(g_conf_size);
        
        //initClientGlobal();
        
        try {
        	ClientGlobal.init(g_conf_file);
        	
            trackerClient = new TrackerClient();
            trackerServer = trackerClient.getConnection();
            
            for(int i = 0; i < g_conf_size; i++){
                storageClient1 = new StorageClient(trackerServer, storageServer);
                idleConnectionPool.add(storageClient1);
                
                logger.info("------------------------- conn + 1：" + idleConnectionPool.size());
            }
            
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
        }
    }
    
    //初始化客户端
    /*private void initClientGlobal(){
        //连接超时时间
        ClientGlobal.setG_connect_timeout(2000);
        //网络超时时间
        ClientGlobal.setG_network_timeout(3000);
        ClientGlobal.setG_anti_steal_token(false);
        // 字符集
        ClientGlobal.setG_charset("UTF-8");
        ClientGlobal.setG_secret_key(null);
        // HTTP访问服务的端口号
        ClientGlobal.setG_tracker_http_port(8080);
         
        InetSocketAddress[] trackerServers = new InetSocketAddress[2];
        trackerServers[0] = new InetSocketAddress("10.64.2.171",22122);
        trackerServers[1] = new InetSocketAddress("10.64.2.172",22122);
        
        TrackerGroup trackerGroup = new TrackerGroup(trackerServers);
        //tracker server 集群
        ClientGlobal.setG_tracker_group(trackerGroup);
    }*/
    
    
    //取出连接
    public static StorageClient checkOut(int waitTime){
        StorageClient storageClient1 = null;
        try {
            storageClient1 = idleConnectionPool.poll(waitTime, TimeUnit.SECONDS);
            
            if(storageClient1 != null){
            	busyConnectionPool.put(storageClient1, obj);
            }
        } catch (InterruptedException e) {
            storageClient1 = null;
            logger.error("busyConnectionPool checkOut fail：" + e.getMessage());
            e.printStackTrace();
        }
        return storageClient1;
    }
    
    //回收连接
    public static void checkIn(StorageClient storageClient1){
        if(busyConnectionPool.remove(storageClient1) != null){
        	idleConnectionPool.add(storageClient1);
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
                
                logger.info("------------------------- conn + 1：" + idleConnectionPool.size());
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
