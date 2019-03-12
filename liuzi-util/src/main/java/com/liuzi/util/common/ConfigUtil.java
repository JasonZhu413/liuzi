package com.liuzi.util.common;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @Title:        ConfigUtil.init(path)
 * 
 * @Description   初始化公共配置文件
 * 
 * @author        ZhuShiyao
 * 
 * @Time          2016-11-23 11:02:29
 * 
 * @version       1.0
 * 
 */
public class ConfigUtil{
	
	private static Logger logger = LoggerFactory.getLogger(ConfigUtil.class);
	
	private static String file_path;
	private static PropertiesConfiguration p;
	
	public static void init(String path){
		file_path = path;
		if(pathIsNotNull()){
			return;
		}
		prop();
	}
	
	private static boolean pathIsNotNull(){
		if(!StringUtils.isEmpty(file_path)){
			return true;
		}
		
		try {
			throw new IllegalArgumentException(">>> WARN!!! 公共配置文件初始化错误，文件地址path为空");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static PropertiesConfiguration prop(){
		if(p != null){
			return p;
		}
		
		if(pathIsNotNull()){
			return null;
		}
		
		LiuziUtil.tag(" --------  Liuzi 公共配置文件初始化...... --------");
		
		logger.info("===== 公共配置文件初始化，加载配置 " + file_path + " ......========");
		
		try{
			p = new PropertiesConfiguration();
			p.setEncoding("UTF-8");
			p.load(file_path);
			logger.info("加载" + file_path + "文件。。。。。。");
	    } catch (Exception e) {
	    	logger.error("加载" + file_path + "文件失败：" + e.getMessage());
	    	e.fillInStackTrace();
	    }
		logger.info("===== 公共配置文件初始化完成 ......========\n");
		
		return p;
	}
	
	public static String getString(String key) {
	    String value = null;
	    try {
	    	value = prop().getString(key);
	    } catch (Exception e) {
	    	logger.error("错误：" + e.getMessage());
	    	e.fillInStackTrace();
	    }
	    return value;
	}
	
	public static String getString(String key, String defaultValue){
		String value = null;
	    try {
	    	value = prop().getString(key, defaultValue);
	    } catch (Exception e) {
	    	logger.error("错误：" + e.getMessage());
	    	e.fillInStackTrace();
	    }
	    return value;
	}
	
	public static int getInt(String key){
		int value = 0;
	    try {
	    	value = prop().getInt(key);
	    } catch (Exception e) {
	    	logger.error("错误：" + e.getMessage());
	    	e.fillInStackTrace();
	    }
	    return value;
	}
	
	public static int getInt(String key, int defaultValue){
		int value = 0;
	    try {
	    	value = prop().getInt(key, defaultValue);
	    } catch (Exception e) {
	    	logger.error("错误：" + e.getMessage());
	    	e.fillInStackTrace();
	    }
	    return value;
	}

	public static long getLong(String key) {
	    long value = 0L;
	    try {
	    	value = prop().getLong(key);
	    } catch (Exception e) {
	    	logger.error("错误：" + e.getMessage());
	    	e.fillInStackTrace();
	    }
	    return value;
	}
	
	public static long getLong(String key, long defaultValue){
		long value = 0L;
	    try {
	    	value = prop().getLong(key, defaultValue);
	    } catch (Exception e) {
	    	logger.error("错误：" + e.getMessage());
	    	e.fillInStackTrace();
	    }
	    return value;
	}

	public static double getDouble(String key) {
	    double value = 0.0D;
	    try {
	    	value = prop().getDouble(key);
	    } catch (Exception e) {
	    	logger.error("错误：" + e.getMessage());
	    	e.fillInStackTrace();
	    }

	    return value;
	}
	
	public static double getDouble(String key, double defaultValue){
		double value = 0.0D;
	    try {
	    	value = prop().getDouble(key, defaultValue);
	    } catch (Exception e) {
	    	logger.error("错误：" + e.getMessage());
	    	e.fillInStackTrace();
	    }

	    return value;
	}

	public static boolean getBoolean(String key) {
	    boolean value = false;
	    try {
	    	value = prop().getBoolean(key);
	    } catch (Exception e) {
	    	logger.error("错误：" + e.getMessage());
	    	e.fillInStackTrace();
	    }
	    return value;
	}
	
	public static boolean getBoolean(String key, boolean defaultValue){
		boolean value = false;
	    try {
	    	value = prop().getBoolean(key, defaultValue);
	    } catch (Exception e) {
	    	logger.error("错误：" + e.getMessage());
	    	e.fillInStackTrace();
	    }
	    return value;
	}

	public static void save(String key, Object value) {
		logger.info(file_path + "保存key: " + key + ", value：" + value);
		
	    try{
	    	PropertiesConfiguration pc = prop();
	    	if(pc == null){
	    		logger.warn("重新加载配置文件失败......");
	    		return;
	    	}
	    	pc.setProperty(key, value);
	    	pc.save(file_path);

	    	pc = new PropertiesConfiguration();
	    	pc.setEncoding("UTF-8");
	    	pc.load(file_path);
	    	logger.info("重新加载配置文件......");
	    } catch (ConfigurationException e) {
	    	logger.error("重新加载配置文件出错：" + e.getMessage());
	    	e.printStackTrace();
	    }
	}
}