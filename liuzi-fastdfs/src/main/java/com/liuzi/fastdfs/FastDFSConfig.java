package com.liuzi.fastdfs;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

<<<<<<< HEAD
import com.liuzi.util.LiuziUtil;

=======
>>>>>>> 02a066291c6197e2d1436dec20fceaf7ded1f342

public class FastDFSConfig {
	
	private static Logger logger = LoggerFactory.getLogger(FastDFSConfig.class);
	
	private final static String DEFAULT_CONF_FILE_NAME = "conf/fdfs.properties";
	
	private static String g_conf_file = DEFAULT_CONF_FILE_NAME;
<<<<<<< HEAD
	protected static TrackerClient tracker;
	protected static TrackerServer trackerServer;
	protected static StorageServer storageServer = null;
	public static StorageClient storageClient;
	
	public FastDFSConfig(){
		init();
	}
=======
	
	public static volatile StorageClient storageClient;
>>>>>>> 02a066291c6197e2d1436dec20fceaf7ded1f342
	
	public FastDFSConfig(String confFile){
		if(!StringUtils.isEmpty(confFile)){
			g_conf_file = confFile;
		}
		init();
	}
	
	public static void init(){
<<<<<<< HEAD
		LiuziUtil.tag("Liuzi FastDFS初始化......");
		
=======
>>>>>>> 02a066291c6197e2d1436dec20fceaf7ded1f342
		logger.info("===== fastdfs初始化，加载配置 " + g_conf_file + " ......========");
		
		try {
			ClientGlobal.init(g_conf_file);
		} catch (Exception e) {
			logger.error("===== fastdfs初始化失败：缺少 " + g_conf_file + " 文件========");
			e.printStackTrace();
			return;
		}
		
		try {
<<<<<<< HEAD
			tracker = new TrackerClient(); 
			logger.info("创建trackerClient实例：new TrackerClient()");
			
			trackerServer = tracker.getConnection(); 
			logger.info("建立连接：tracker.getConnection()");
            
=======
			TrackerClient tracker = new TrackerClient(); 
			logger.info("创建trackerClient实例：new TrackerClient()");
			
			TrackerServer trackerServer = tracker.getConnection(); 
			logger.info("建立连接：tracker.getConnection()");
			
            StorageServer storageServer = null;
>>>>>>> 02a066291c6197e2d1436dec20fceaf7ded1f342
            storageClient = new StorageClient(trackerServer, storageServer); 
            logger.info("创建storageClient实例：new StorageClient(trackerServer, storageServer)");
		} catch (Exception e) {
			logger.error("===== fastdfs初始化失败：" + e.getMessage());
			e.printStackTrace();
			return;
		}
		
		logger.info("===== fastdfs初始化完成 ......========");
	}
}
