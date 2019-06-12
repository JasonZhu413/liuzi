package com.liuzi.util.common;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.util.StringUtils;



/**
 * @Title:        
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
@Slf4j
public class ConfigUtil{
	
	private static String configPath = "";
	private static PropertiesConfiguration p;
	
	public static void init(String path){
		if(p == null){
			configPath = path;
			if(pathNotNull()){
				prop();
			}
		}
	}
	
	private static boolean pathNotNull(){
		if(StringUtils.isEmpty(configPath)){
			throw new IllegalArgumentException(">>> WARN!!! ConfigUtil path is null");
		}
		return true;
	}
	
	private static PropertiesConfiguration prop(){
		if(p == null && pathNotNull()){
			LiuziUtil.tag(" --------  Liuzi ConfigUtil init --------");
			try{
				log.info(" path: " + configPath);
				p = new PropertiesConfiguration();
				p.setEncoding("UTF-8");
				p.load(configPath);
		    } catch (Exception e) {
		    	log.error(" --------  ConfigUtil(" + configPath + ") init fail：" +
		    			e.getMessage() + " --------");
		    }
		    log.info(" --------  ConfigUtil init success --------  ");
		}
		return p;
	}
	
	public static String getString(String key) {
	    String value = null;
	    try {
	    	value = prop().getString(key);
	    } catch (Exception e) {
	    	log.error("错误：" + e.getMessage());
	    	e.fillInStackTrace();
	    }
	    return value;
	}
	
	public static String getString(String key, String defaultValue){
		String value = defaultValue;
	    try {
	    	value = prop().getString(key, defaultValue);
	    } catch (Exception e) {
	    	log.error("错误：" + e.getMessage());
	    	e.fillInStackTrace();
	    }
	    return value;
	}
	
	public static int getInt(String key){
		int value = 0;
	    try {
	    	value = prop().getInt(key);
	    } catch (Exception e) {
	    	log.error("错误：" + e.getMessage());
	    	e.fillInStackTrace();
	    }
	    return value;
	}
	
	public static int getInt(String key, int defaultValue){
		int value = 0;
	    try {
	    	value = prop().getInt(key, defaultValue);
	    } catch (Exception e) {
	    	log.error("错误：" + e.getMessage());
	    	e.fillInStackTrace();
	    }
	    return value;
	}

	public static long getLong(String key) {
	    long value = 0L;
	    try {
	    	value = prop().getLong(key);
	    } catch (Exception e) {
	    	log.error("错误：" + e.getMessage());
	    	e.fillInStackTrace();
	    }
	    return value;
	}
	
	public static long getLong(String key, long defaultValue){
		long value = 0L;
	    try {
	    	value = prop().getLong(key, defaultValue);
	    } catch (Exception e) {
	    	log.error("错误：" + e.getMessage());
	    	e.fillInStackTrace();
	    }
	    return value;
	}

	public static double getDouble(String key) {
	    double value = 0.0D;
	    try {
	    	value = prop().getDouble(key);
	    } catch (Exception e) {
	    	log.error("错误：" + e.getMessage());
	    	e.fillInStackTrace();
	    }

	    return value;
	}
	
	public static double getDouble(String key, double defaultValue){
		double value = 0.0D;
	    try {
	    	value = prop().getDouble(key, defaultValue);
	    } catch (Exception e) {
	    	log.error("错误：" + e.getMessage());
	    	e.fillInStackTrace();
	    }

	    return value;
	}

	public static boolean getBoolean(String key) {
	    boolean value = false;
	    try {
	    	value = prop().getBoolean(key);
	    } catch (Exception e) {
	    	log.error("错误：" + e.getMessage());
	    	e.fillInStackTrace();
	    }
	    return value;
	}
	
	public static boolean getBoolean(String key, boolean defaultValue){
		boolean value = false;
	    try {
	    	value = prop().getBoolean(key, defaultValue);
	    } catch (Exception e) {
	    	log.error("错误：" + e.getMessage());
	    	e.fillInStackTrace();
	    }
	    return value;
	}
}