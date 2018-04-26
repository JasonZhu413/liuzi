package com.liuzi.memcached;

import java.io.IOException;  
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

import com.liuzi.util.LiuziUtil;
import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;
  
/** 
 *  
 * @ClassName: MemcachedUtils 
 * @Description: Memcached工具类 
 * @author yinjw 
 * @date 2014-6-18 下午5:28:08 
 *  
 */  
@Slf4j
public class MemcachedConfig {  
	private final static String POOL_NAME = "memcachedPool";
	private final static String CONFIG_FILE = "conf/memcached.properties";
	private static String g_conf_file = CONFIG_FILE;
	
	protected static MemCachedClient cachedClient;
	protected static Properties properties;
    
    public MemcachedConfig(){
    	init();
    }  
    
    public MemcachedConfig(String confFile) {
    	if(!StringUtils.isEmpty(confFile)){
			g_conf_file = confFile;
		}
    	init();
    } 
	
	public static void init(){
		LiuziUtil.tag("  --------  Liuzi Memcached初始化......  --------");
		
		log.info("===== memcached初始化，加载配置 " + g_conf_file + " ......========");
		
		if(properties == null){
			try (InputStream in = PropertyUtils.class.getClassLoader().getResourceAsStream(g_conf_file)){
				properties = new Properties();
				properties.load(in);
			} catch (IOException e) {
				log.error("memcached初始化失败，错误：" + e.getMessage());
				e.printStackTrace();
				return;
			}
		}
		
		String m_server = properties.getProperty("memcached.server");
    	String m_initConn = properties.getProperty("memcached.initConn");
    	String m_minConn = properties.getProperty("memcached.minConn");
    	String m_maxConn = properties.getProperty("memcached.maxConn");
    	String m_maintSleep = properties.getProperty("memcached.maintSleep");
    	String m_nagle = properties.getProperty("memcached.nagle");
    	String m_socketTO = properties.getProperty("memcached.socketTO");
    	
    	SockIOPool pool = SockIOPool.getInstance(POOL_NAME);
    	pool.setServers(m_server.split(","));
    	pool.setInitConn(Integer.parseInt(m_initConn));
    	pool.setMinConn(Integer.parseInt(m_minConn));
    	pool.setMaxConn(Integer.parseInt(m_maxConn));
    	pool.setMaintSleep(Integer.parseInt(m_maintSleep));
    	pool.setNagle(Boolean.parseBoolean(m_nagle));
    	pool.setSocketTO(Integer.parseInt(m_socketTO));
    	pool.initialize();
    	
    	cachedClient = new MemCachedClient(POOL_NAME);
    	cachedClient.setSanitizeKeys(false);
    	
    	log.info("===== memcached初始化完成 ......========");
	}
  
}  
