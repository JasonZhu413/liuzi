package com.liuzi.util.common;

import java.io.File;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import com.alibaba.fastjson.JSONObject;



@Slf4j
public class Log{
	
	private static final String ENTER = "\\".equals(File.separator) ? "\r\n" : "\n";
	
	public static void debug(String msg) {
	    log.debug(msg);
	}

	public static void debug(String msg, Object[] params) {
	    StringBuffer debug = new StringBuffer("");
	    debug.append(msg);
	    debug.append(getMsgParams(params));
	    log.debug(debug.toString());
	}
	
	public static void info(String msg) {
	    log.info(msg);
	}
	
	public static void info(String msg, String args){
	    StringBuffer info = new StringBuffer();
	    info.append(msg);
	    info.append(ENTER);
	    info.append(args);
	    info.append(ENTER);
	    log.info(info.toString());
	}
	
	public static void info(String msg, Object[] params) {
		StringBuffer info = new StringBuffer("");
		info.append(msg);
		info.append(getMsgParams(params));
		log.info(info.toString());
	}
	
	public static void info(String msg, Map<String, Object> params) {
	    StringBuffer info = new StringBuffer("");
	    info.append(msg);
	    info.append(JSONObject.toJSON(params).toString());
		log.info(info.toString()); 
	}
	
	public static void warn(String msg) {
	    log.warn(msg);
	}
	
	public static void warn(String msg, String args){
	    String newLine = "\n";
	    if ("\\".equals(File.separator)) {
	    	newLine = "\r\n";
	    }
	    StringBuffer info = new StringBuffer();
	    info.append(msg);
	    info.append(newLine);
	    info.append(newLine);
	    info.append(args);
	    info.append(newLine);
	    log.warn(info.toString());
	}
	
	public static void warn(String msg, Object[] params) {
		StringBuffer info = new StringBuffer("");
		info.append(msg);
		info.append(getMsgParams(params));
		log.warn(info.toString());
	}
	
	public static void warn(String msg, Map<String, Object> params) {
	    StringBuffer info = new StringBuffer("");
	    info.append(msg);
	    info.append(JSONObject.toJSON(params).toString());
		log.warn(info.toString()); 
	}

	public static void error(String msg, Exception e){
		log.error(msg, e);
	}
	
	public static void error(String msg, Exception e, Object[] params){
	    String newLine = "\n";
	    if ("\\".equals(File.separator)) {
	    	newLine = "\r\n";
	    }
	    StringBuffer error = new StringBuffer();
	    error.append(msg);
	    error.append(newLine);
	    error.append(getMsgParams(params));
	    log.error(error.toString(), e);
	}

	public static void error(String msg, Exception e, Map<String, String> mapParams) {
		StringBuffer error = new StringBuffer("");
		error.append(msg);
		error.append(getMsgParams(mapParams));
		log.error(error.toString(), e);
	}

	public static void error(String msg, Exception e, Map<String, String> mapParams, Object[] params) {
		StringBuffer error = new StringBuffer("");
		error.append(msg);
		error.append(getMsgParams(mapParams, params));
		log.error(error.toString(), e);
	}

	private static String getMsgParams(Object[] params){
		StringBuffer returnStr = new StringBuffer();
	    if ((params != null) && (params.length > 0)) {
	    	returnStr.append("-- Params --{");
	    	int i = 1;
	    	for (Object param : params) {
	    		if (i % 2 == 1)
	    			returnStr.append("[" + param + ":");
	    		else {
	    			returnStr.append(param + "]");
	    		}
	    		++i;
	    	}
	    	returnStr.append("}");
	    }
	    return returnStr.toString();
	}

	private static String getMsgParams(Map<String, String> mapParams){
	    StringBuffer returnStr = new StringBuffer();
	    returnStr.append("-- Params --{");
	    if (mapParams != null) {
	    	for (Map.Entry<String, String> entry : mapParams.entrySet()) {
	    		returnStr.append("[" + ((String)entry.getKey()) + ":" + 
	    				((String)entry.getValue()) + "]");
	    	}
	    }
	    returnStr.append("}");
	    return returnStr.toString();
	}

	private static String getMsgParams(Map<String, String> mapParams, Object[] params){
	    StringBuffer returnStr = new StringBuffer();
	    if ((params != null) && (params.length > 0)){
	    	returnStr.append("-- Params --{");
	    	int i = 1;
	    	for (Object param : params) {
	    		if (i % 2 == 1)
	    			returnStr.append("[" + param + ":");
	    		else {
	    			returnStr.append(param + "]");
	    		}
	    		++i;
	    	}

	    	if (mapParams != null) {
	    		for (Map.Entry<String, String> entry : mapParams.entrySet()) {
	    			returnStr.append("[" + ((String)entry.getKey()) + ":" + 
	    					((String)entry.getValue()) + "]");
	    		}
	    	}

	    	returnStr.append("}");
	    }

	    return returnStr.toString();
	}
	
	public static void main(String[] args) {
		info("aaa{},{}", new String[]{"sda", "d"});
	}
}
