package com.liuzi.util.common;

import java.util.Map;
import java.util.Map.Entry;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.util.StringUtils;

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
			Log.info("加载配置 {} ...", path);
	    } catch (Exception e) {
	    	Log.error(e, "加载配置错误 {} ...", path);
	    }
	}
	
	public String getString(String key) {
	    String value = null;
	    try {
	    	value = prop.getString(key);
	    } catch (Exception e) {
	    	Log.error(e, "读取String配置错误，key：{} ...", key);
	    }
	    return value;
	}
	
	public String getString(String key, String defaultValue){
		String value = null;
	    try {
	    	value = prop.getString(key, defaultValue);
	    } catch (Exception e) {
	    	Log.error(e, "读取String配置错误，key：{} ...", key);
	    }
	    return value;
	}
	
	public int getInt(String key){
		int value = 0;
	    try {
	    	value = prop.getInt(key);
	    } catch (Exception e) {
	    	Log.error(e, "读取int配置错误，key：{} ...", key);
	    }
	    return value;
	}
	
	public int getInt(String key, int defaultValue){
		int value = 0;
	    try {
	    	value = prop.getInt(key, defaultValue);
	    } catch (Exception e) {
	    	Log.error(e, "读取int配置错误，key：{} ...", key);
	    }
	    return value;
	}

	public long getLong(String key) {
	    long value = 0L;
	    try {
	    	value = prop.getLong(key);
	    } catch (Exception e) {
	    	Log.error(e, "读取long配置错误，key：{} ...", key);
	    }
	    return value;
	}
	
	public long getLong(String key, long defaultValue){
		long value = 0L;
	    try {
	    	value = prop.getLong(key, defaultValue);
	    } catch (Exception e) {
	    	Log.error(e, "读取long配置错误，key：{} ...", key);
	    }
	    return value;
	}

	public double getDouble(String key) {
	    double value = 0.0D;
	    try {
	    	value = prop.getDouble(key);
	    } catch (Exception e) {
	    	Log.error(e, "读取double配置错误，key：{} ...", key);
	    }

	    return value;
	}
	
	public double getDouble(String key, double defaultValue){
		double value = 0.0D;
	    try {
	    	value = prop.getDouble(key, defaultValue);
	    } catch (Exception e) {
	    	Log.error(e, "读取double配置错误，key：{} ...", key);
	    }

	    return value;
	}

	public boolean getBoolean(String key) {
	    boolean value = false;
	    try {
	    	value = prop.getBoolean(key);
	    } catch (Exception e) {
	    	Log.error(e, "读取boolean配置错误，key：{} ...", key);
	    }
	    return value;
	}
	
	public boolean getBoolean(String key, boolean defaultValue){
		boolean value = false;
	    try {
	    	value = prop.getBoolean(key, defaultValue);
	    } catch (Exception e) {
	    	Log.error(e, "读取boolean配置错误，key：{} ...", key);
	    }
	    return value;
	}

	public void save(String key, Object value) {
		String path = prop.getBasePath();
		Log.info("保存配置，path: {}, key: {}, value: {}", path, key, value);
	    try{
	    	prop.setProperty(key, value);
	    	prop.save(path);
	    	prop.load(path);
	    } catch (ConfigurationException e) {
	    	Log.error(e, "保存配置错误，path: {}, key: {}, value: {}", path, key, value);
	    }
	}
	
	public void saveBatch(Map<String, Object> map){
		String path = prop.getBasePath();
	    try{
	    	if(map == null || map.isEmpty()){
	    		return;
	    	}
	    	
	    	Log.info("批量保存配置，path: {}, keys.length: {}", path, map.size());
	    	
	    	for(Entry<String, Object> entry : map.entrySet()){
	    		String key = entry.getKey();
	    		if(StringUtils.isEmpty(key)){
	    			continue;
	    		}
	    		prop.setProperty(key, entry.getValue());
	    	}
	    	prop.save(path);
	    	prop.load(path);
	    } catch (ConfigurationException e) {
	    	Log.error(e, "保存配置错误，path: {}", path);
	    }
	}
}
