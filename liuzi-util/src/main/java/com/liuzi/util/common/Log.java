package com.liuzi.util.common;

import java.io.File;
import java.util.Map;



import org.slf4j.Logger;

import com.alibaba.fastjson.JSONObject;

public class Log{
  public static void error(String msg, Logger logger, Exception e, Object[] params){
    String newLine = "\n";
    if ("\\".equals(File.separator)) {
      newLine = "\r\n";
    }
    StringBuffer error = new StringBuffer();
    error.append(msg);
    error.append(newLine);
    error.append(getMsgParams(params));
    logger.error(error.toString(), e);
  }

  public static void info(String msg, Logger logger, String args){
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
    logger.info(info.toString());
  }

  private static String getMsgParams(Object[] params)
  {
    StringBuffer returnStr = new StringBuffer();

    if ((params != null) && (params.length > 0)) {
      returnStr.append("--参数--{");
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

  private static String getMsgParams(Map<String, String> mapParams)
  {
    StringBuffer returnStr = new StringBuffer();
    returnStr.append("--参数--{");
    if (mapParams != null) {
      for (Map.Entry<String, String> entry : mapParams.entrySet()) {
        returnStr.append("[" + ((String)entry.getKey()) + ":" + ((String)entry.getValue()) + "]");
      }
    }
    returnStr.append("}");
    return returnStr.toString();
  }

  private static String getMsgParams(Map<String, String> mapParams, Object[] params)
  {
    StringBuffer returnStr = new StringBuffer();
    if ((params != null) && (params.length > 0))
    {
      returnStr.append("--参数--{");
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
          returnStr.append("[" + ((String)entry.getKey()) + ":" + ((String)entry.getValue()) + "]");
        }
      }

      returnStr.append("}");
    }

    return returnStr.toString();
  }

  public static void error(Logger logger, String msg, Exception e)
  {
    logger.error(msg, e);
  }

  public static void error(Logger logger, String msg, Exception e, Object[] params) {
    StringBuffer error = new StringBuffer("");
    error.append(msg);
    error.append(getMsgParams(params));
    logger.error(error.toString(), e);
  }

  public static void error(Logger logger, String uuid, String msg, Exception e, Object[] params) {
    StringBuffer error = new StringBuffer("");
    error.append("[UUID:");
    error.append(uuid);
    error.append("]");
    error.append("[Error Message:]");
    error.append(msg);
    error.append(getMsgParams(params));
    logger.error(error.toString(), e);
  }

  public static void error(Logger logger, String msg, Exception e, Map<String, String> mapParams) {
    StringBuffer error = new StringBuffer("");
    error.append(msg);
    error.append(getMsgParams(mapParams));
    logger.error(error.toString(), e);
  }

  public static void error(Logger logger, String uuid, String msg, Exception e, Map<String, String> mapParams) {
    StringBuffer error = new StringBuffer("");
    error.append("[UUID:");
    error.append(uuid);
    error.append("]");
    error.append("[Error Message:]");
    error.append(msg);
    error.append(getMsgParams(mapParams));
    logger.error(error.toString(), e);
  }

  public static void error(Logger logger, String msg, Exception e, Map<String, String> mapParams, Object[] params) {
    StringBuffer error = new StringBuffer("");
    error.append(msg);
    error.append(getMsgParams(mapParams, params));
    logger.error(error.toString(), e);
  }

  public static void error(Logger logger, String uuid, String msg, Exception e, Map<String, String> mapParams, Object[] params) {
    StringBuffer error = new StringBuffer("");
    error.append("[UUID:");
    error.append(uuid);
    error.append("]");
    error.append("[Error Message:]");
    error.append(msg);
    error.append(getMsgParams(mapParams, params));
    logger.error(error.toString(), e);
  }

  public static void info(Logger logger, String msg) {
    logger.info(msg);
  }

  public static void info(Logger logger, String msg, Object[] params) {
    StringBuffer info = new StringBuffer("");
    info.append(msg);
    info.append(getMsgParams(params));
    logger.debug(info.toString());
  }

  public static void debug(Logger logger, String msg) {
    logger.debug(msg);
  }

  public static void debug(Logger logger, String msg, Object[] params) {
    StringBuffer debug = new StringBuffer("");
    debug.append(msg);
    debug.append(getMsgParams(params));
    logger.debug(debug.toString());
  }
  
  public static void info(Logger logger, String uuid, String msg, String params) {
    StringBuffer info = new StringBuffer("");
    info.append("[UUID:");
    info.append(uuid);
    info.append("]");
    info.append("[Info Message:");
    info.append(msg);
    info.append("]");
    info.append("[Params:");
    info.append(params);
    info.append("]");
    logger.info(info.toString()); 
}
  
  public static void info(Logger logger, String uuid, String msg, Map<String, Object> params) {
	    StringBuffer info = new StringBuffer("");
	    info.append("[UUID:");
	    info.append(uuid);
	    info.append("]");
	    info.append("[Info Message:");
	    info.append(msg);
	    info.append("]");
	    info.append("[Params:");
	    info.append(JSONObject.toJSON(params).toString());
	    info.append("]");
	    logger.info(info.toString()); 
  }

  public static void info(Logger logger, String uuid, String msg, Object[] params) {
    StringBuffer info = new StringBuffer("");
    info.append("[UUID:");
    info.append(uuid);
    info.append("]");
    info.append("[Info Message:]");
    info.append(msg);
    info.append(getMsgParams(params));
    logger.info(info.toString()); 
  }

  public static String getLogStr(Logger logger, String uuid, String msg, Object[] params) {
    StringBuffer info = new StringBuffer("");
    info.append("[UUID:");
    info.append(uuid);
    info.append("]");
    info.append("[Info Message:]");
    info.append(msg);
    info.append(getMsgParams(params));
    return info.toString();
  }
}
