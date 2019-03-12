package com.liuzi.util.common;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class PropertiesUtil extends PropertiesConfiguration{
	
	private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
	private PropertiesConfiguration prop;
	
	public PropertiesUtil(String path){
		if(StringUtils.isEmpty(path)){
			logger.warn("警告：配置文件目录path为空");
			return;
		}
		
		prop = new PropertiesConfiguration();
		prop.setEncoding("UTF-8");
		
		try{
			prop.load(path);
			logger.info("加载" + path + "文件......");
	    } catch (Exception e) {
	    	logger.error("加载" + path + "文件失败：" + e.getMessage());
	    	e.fillInStackTrace();
	    }
	}
	
	public String getString(String key) {
	    String value = null;
	    try {
	    	value = prop.getString(key);
	    } catch (Exception e) {
	    	logger.error("错误：" + e.getMessage());
	    	e.fillInStackTrace();
	    }
	    return value;
	}
	
	public String getString(String key, String defaultValue){
		String value = null;
	    try {
	    	value = prop.getString(key, defaultValue);
	    } catch (Exception e) {
	    	logger.error("错误：" + e.getMessage());
	    	e.fillInStackTrace();
	    }
	    return value;
	}
	
	public int getInt(String key){
		int value = 0;
	    try {
	    	value = prop.getInt(key);
	    } catch (Exception e) {
	    	logger.error("错误：" + e.getMessage());
	    	e.fillInStackTrace();
	    }
	    return value;
	}
	
	public int getInt(String key, int defaultValue){
		int value = 0;
	    try {
	    	value = prop.getInt(key, defaultValue);
	    } catch (Exception e) {
	    	logger.error("错误：" + e.getMessage());
	    	e.fillInStackTrace();
	    }
	    return value;
	}

	public long getLong(String key) {
	    long value = 0L;
	    try {
	    	value = prop.getLong(key);
	    } catch (Exception e) {
	    	logger.error("错误：" + e.getMessage());
	    	e.fillInStackTrace();
	    }
	    return value;
	}
	
	public long getLong(String key, long defaultValue){
		long value = 0L;
	    try {
	    	value = prop.getLong(key, defaultValue);
	    } catch (Exception e) {
	    	logger.error("错误：" + e.getMessage());
	    	e.fillInStackTrace();
	    }
	    return value;
	}

	public double getDouble(String key) {
	    double value = 0.0D;
	    try {
	    	value = prop.getDouble(key);
	    } catch (Exception e) {
	    	logger.error("错误：" + e.getMessage());
	    	e.fillInStackTrace();
	    }

	    return value;
	}
	
	public double getDouble(String key, double defaultValue){
		double value = 0.0D;
	    try {
	    	value = prop.getDouble(key, defaultValue);
	    } catch (Exception e) {
	    	logger.error("错误：" + e.getMessage());
	    	e.fillInStackTrace();
	    }

	    return value;
	}

	public boolean getBoolean(String key) {
	    boolean value = false;
	    try {
	    	value = prop.getBoolean(key);
	    } catch (Exception e) {
	    	logger.error("错误：" + e.getMessage());
	    	e.fillInStackTrace();
	    }
	    return value;
	}
	
	public boolean getBoolean(String key, boolean defaultValue){
		boolean value = false;
	    try {
	    	value = prop.getBoolean(key, defaultValue);
	    } catch (Exception e) {
	    	logger.error("错误：" + e.getMessage());
	    	e.fillInStackTrace();
	    }
	    return value;
	}

	public void save(String key, Object value) {
		String path = prop.getBasePath();
		logger.info(path + "保存key: " + key + ", value：" + value);
	    try{
	    	prop.setProperty(key, value);
	    	prop.save(path);
	    	prop.load(path);
	    	logger.info("重新加载配置文件......");
	    } catch (ConfigurationException e) {
	    	logger.error("重新加载配置文件出错：" + e.getMessage());
	    	e.printStackTrace();
	    }
	}
	
	public static void main(String[] args) {
		//new PropertiesUtil("").getString(key, defaultValue);
	}
}
