package com.liuzi.util;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;



/**
 * @Title:        ConfigUtil
 * 
 * @Description   获取application.properties属性内容
 * 
 * @author        ZhuShiyao
 * 
 * @Time          2016-11-23 11:02:29
 * 
 * @version       1.0
 * 
 */
public class ConfigUtil {
	
	private static Logger logger = LoggerFactory.getLogger(ConfigUtil.class);
	
	private static PropertiesConfiguration config;
	private static final String CONFIG_FILE = "conf/application.properties";
	
	private static String conf_file = CONFIG_FILE;
	
	public ConfigUtil(String filePath){
		if(!StringUtils.isEmpty(filePath)){
			conf_file = filePath;
		}
		init();
	}
	
	public static void init(){
		config = new PropertiesConfiguration();
		config.setEncoding("UTF-8");
		
		try{
			config.load(conf_file);
			logger.info("初始化" + conf_file + "文件。。。。。。");
	    } catch (Exception e) {
	    	logger.error("初始化" + conf_file + "文件失败：" + e.getMessage());
	    	e.fillInStackTrace();
	    }
	}
	
	static{
		if (config == null){
		    synchronized(ConfigUtil.class) {
		    	if (config == null){
		    		init();
		    	}
		    }
		}
	}
	
	public static Integer getIntValue(String key){
		if(!is_init(key)) return null;
			
	    int value = 0;
	    try {
	    	value = config.getInt(key);
	    } catch (Exception e) {
	    	logger.error("错误：" + e.getMessage());
	    	e.fillInStackTrace();
	    }
	    return value;
	}
	
	public static Integer getIntValue(String key, String url){
	    return (int) reload(key, url);
	}

	public static Long getLongValue(String key) {
		if(!is_init(key)) return null;
		
	    long value = 0L;
	    try {
	    	value = config.getLong(key);
	    } catch (Exception e) {
	    	logger.error("错误：" + e.getMessage());
	    	e.fillInStackTrace();
	    }

	    return value;
	}
	
	public static Long getLongValue(String key, String url){
	    return (long) reload(key, url);
	}

	public static Double getDoubleValue(String key) {
		if(!is_init(key)) return null;
		
	    double value = 0.0D;
	    try {
	    	value = config.getDouble(key);
	    } catch (Exception e) {
	    	logger.error("错误：" + e.getMessage());
	    	e.fillInStackTrace();
	    }

	    return value;
	}
	
	public static Double getDoubleValue(String key, String url){
	    return (double) reload(key, url);
	}

	public static String getStringValue(String key) {
		if(!is_init(key)) return null;
		
	    String value = "";
	    try {
	    	value = config.getString(key);
	    } catch (Exception e) {
	    	logger.error("错误：" + e.getMessage());
	    	e.fillInStackTrace();
	    }
	    return value;
	}
	
	public static String getStringValue(String key, String url){
	    return reload(key, url).toString();
	}

	public static Boolean getBooleanValue(String key) {
		if(!is_init(key)) return null;
		
	    boolean value = false;
	    try {
	    	value = config.getBoolean(key);
	    } catch (Exception e) {
	    	logger.error("错误：" + e.getMessage());
	    	e.fillInStackTrace();
	    }

	    return value;
	}
	
	public static Boolean getBooleanValue(String key, String url){
	    return (boolean) reload(key, url);
	}

	public static void save(String key, Object value) {
		if(!is_init(key)) return;
			
		logger.info("保存key：" + key + "，value：" + value + "......");
		config.setProperty(key, value);
	    try{
	    	config.save(CONFIG_FILE);

	    	config = new PropertiesConfiguration();
	    	config.setEncoding("UTF-8");
	    	config.load(CONFIG_FILE);
	    	logger.info("重新加载配置文件......");
	    } catch (ConfigurationException e) {
	    	logger.error("重新加载配置文件出错：" + e.getMessage());
	    	e.printStackTrace();
	    }
	}
	
	public static Object reload(String key, String url){
		if(StringUtil.empty(key)){
			logger.warn("参数" + key + "为空......");
			return null;
		}
		logger.info("加载配置文件" + url + "，获取参数" + key + "......");
		PropertiesConfiguration config = new PropertiesConfiguration();
		config.setEncoding("UTF-8");
		String value = "";
	    try{
	    	config.load(url);
	    	value = config.getString(key);
	    } catch (ConfigurationException e) {
	    	logger.error("加载配置文件错误：" + e.getMessage());
	    	e.printStackTrace();
	    }
	    
	    return value;
	}
	
	private static boolean is_init(String key){
		if(StringUtils.isEmpty(key) || config == null){
			logger.warn("未初始化或参数key为空......");
			return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
		System.out.println(ConfigUtil.getStringValue(""));
	}
}