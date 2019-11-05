package com.liuzi.util.common;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Log{
	
	public static final String TRACE_LOG_ID = "TRACE_LOG_ID";
	
	/**
	 * 设置traceid
	 */
	public static void setLogId(String traceId){
		if(StringUtils.isBlank(traceId)){
			initTraceLogId();
		}else{
			MDC.put(TRACE_LOG_ID, traceId);
		}
	}
		
	/**
	 * 获取traceid
	 */
	public static String getLogId(){
		return MDC.get(TRACE_LOG_ID);
	}
	
	/**
	 * 清除traceid
	 */
	public static void clearLogId(){
		MDC.clear();
	}
	
	public static void debug(String msg){
		if(!isDebugEnabled()){
			return;
		}
		log.debug(msg);
	}
	
	public static void debug(String msg, Object... params){
		if(!isDebugEnabled()){
			return;
		}
		log.debug(msg, params);
	}
	
	public static void debug(Exception e, String msg){
		if(!isDebugEnabled()){
			return;
		}
		log.debug(msg, e);
	}
	
	public static void debug(Exception e, String msg, Object... params){
		if(!isDebugEnabled()){
			return;
		}
		log.debug(arrayMsg(msg, params), e);
	}
	
	public static void info(String msg){
		initTraceLogId();
		log.info(msg);
	}
	
	public static void info(String msg, Object... params){
		initTraceLogId();
		log.info(msg, params);
	}
	
	public static void info(Exception e, String msg){
		initTraceLogId();
		log.info(msg, e);
	}
	
	public static void info(Exception e, String msg, Object... params){
		initTraceLogId();
		log.info(arrayMsg(msg, params), e);
	}
	
	public static void warn(String msg){
		initTraceLogId();
		log.warn(msg);
	}
	
	public static void warn(String msg, Object... params){
		initTraceLogId();
		log.warn(msg, params);
	}
	
	public static void warn(Exception e, String msg){
		initTraceLogId();
		log.warn(msg, e);
	}
	
	public static void warn(Exception e, String msg, Object... params){
		initTraceLogId();
		log.warn(arrayMsg(msg, params), e);
	}
	
	public static void error(String msg){
		initTraceLogId();
		log.error(msg);
	}
	
	public static void error(String msg, Object... params){
		initTraceLogId();
		log.error(msg, params);
	}
	
	public static void error(Exception e, String msg){
		initTraceLogId();
		log.error(msg, e);
	}
	
	public static void error(Exception e, String msg, Object... params){
		initTraceLogId();
		log.error(arrayMsg(msg, params), e);
	}
	
	private static boolean isDebugEnabled(){
		if(log.isDebugEnabled()){
			initTraceLogId();
			return true;
		}
		return false;
	}
	
	private static String initTraceLogId(){
		String traceLogid = MDC.get(TRACE_LOG_ID);
		if(traceLogid == null){
			MDC.put(TRACE_LOG_ID, UUID.randomUUID().toString());
		}
		return traceLogid;
	}
	
	private static String arrayMsg(String msg, Object... params){
		if(params == null || params.length == 0){
			return msg;
		}
		return String.format(msg.replace("{}", "%s"), params);
	}
	
	/**
	 * 构建方法信息
	 */
	/*private static void buildMethod(String msg) {
        StackTraceElement[] stes = Thread.currentThread().getStackTrace();
        StackTraceElement ste = stes[3];
        builder.append("Class: ").append(ste.getClassName()).append(", ");
        builder.append("Method: ").append(ste.getMethodName()).append(", ");
        builder.append("Line: ").append(ste.getLineNumber()).append(", ");
        builder.append("Msg: ");
    }*/
	
	/**
	 * 打印异常堆栈信息
	 * @param msg
	 * @param params
	 * @return
	 */
    /*private static String getStackTrace(Exception ex){//(Throwable ex) {
        if(isDebugEnabled()){
        	String msg = "";
            StackTraceElement[] traceElements = ex.getStackTrace();
            if (traceElements != null && traceElements.length > 0) {
            	StringBuilder traceBuilder = new StringBuilder();
                for (StackTraceElement traceElement : traceElements) {
                    traceBuilder.append(traceElement.toString());
                    traceBuilder.append(" <<< ");
                }
                msg = traceBuilder.toString();
                log.error("ERROR position: {}", msg, ex);
            }
            return msg;
        }
        return "";
    }*/
 
    //构造异常堆栈信息
    /*public static String buildError(Exception ex) {
        return String.format("%s : %s \r\n %s", ex.toString(), 
        		ex.getMessage(), getStackTrace(ex));
    }*/
	
}
