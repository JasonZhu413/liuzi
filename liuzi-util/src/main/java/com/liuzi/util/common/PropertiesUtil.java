package com.liuzi.util.common;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.util.StringUtils;

@Slf4j
public class PropertiesUtil extends PropertiesConfiguration{
	
	private PropertiesConfiguration prop;
	
	public PropertiesUtil(String path){
		if(StringUtils.isEmpty(path)){
			throw new IllegalArgumentException(">>> WARN!!! Properties path is null");
		}
		
		prop = new PropertiesConfiguration();
		prop.setEncoding("UTF-8");
		try{
			prop.load(path);
			log.info("Properties load " + path + "...");
	    } catch (Exception e) {
	    	log.error("Properties load " + path + " fail：" + e.getMessage());
	    }
	}
	
	public String getString(String key) {
	    String value = null;
	    try {
	    	value = prop.getString(key);
	    } catch (Exception e) {
	    	log.error("Error：" + e.getMessage());
	    }
	    return value;
	}
	
	public String getString(String key, String defaultValue){
		String value = null;
	    try {
	    	value = prop.getString(key, defaultValue);
	    } catch (Exception e) {
	    	log.error("Error：" + e.getMessage());
	    }
	    return value;
	}
	
	public int getInt(String key){
		int value = 0;
	    try {
	    	value = prop.getInt(key);
	    } catch (Exception e) {
	    	log.error("Error：" + e.getMessage());
	    }
	    return value;
	}
	
	public int getInt(String key, int defaultValue){
		int value = 0;
	    try {
	    	value = prop.getInt(key, defaultValue);
	    } catch (Exception e) {
	    	log.error("Error：" + e.getMessage());
	    }
	    return value;
	}

	public long getLong(String key) {
	    long value = 0L;
	    try {
	    	value = prop.getLong(key);
	    } catch (Exception e) {
	    	log.error("Error：" + e.getMessage());
	    }
	    return value;
	}
	
	public long getLong(String key, long defaultValue){
		long value = 0L;
	    try {
	    	value = prop.getLong(key, defaultValue);
	    } catch (Exception e) {
	    	log.error("Error：" + e.getMessage());
	    }
	    return value;
	}

	public double getDouble(String key) {
	    double value = 0.0D;
	    try {
	    	value = prop.getDouble(key);
	    } catch (Exception e) {
	    	log.error("Error：" + e.getMessage());
	    }

	    return value;
	}
	
	public double getDouble(String key, double defaultValue){
		double value = 0.0D;
	    try {
	    	value = prop.getDouble(key, defaultValue);
	    } catch (Exception e) {
	    	log.error("Error：" + e.getMessage());
	    }

	    return value;
	}

	public boolean getBoolean(String key) {
	    boolean value = false;
	    try {
	    	value = prop.getBoolean(key);
	    } catch (Exception e) {
	    	log.error("Error：" + e.getMessage());
	    }
	    return value;
	}
	
	public boolean getBoolean(String key, boolean defaultValue){
		boolean value = false;
	    try {
	    	value = prop.getBoolean(key, defaultValue);
	    } catch (Exception e) {
	    	log.error("Error：" + e.getMessage());
	    }
	    return value;
	}

	public void save(String key, Object value) {
		String path = prop.getBasePath();
		log.info(path + "Properties save key: " + key + ", value：" + value);
	    try{
	    	prop.setProperty(key, value);
	    	prop.save(path);
	    	prop.load(path);
	    	log.info("Properties reload...");
	    } catch (ConfigurationException e) {
	    	log.error("Properties save error：" + e.getMessage());
	    }
	}
}
