package com.liuzi.util.optimization;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;



//@Configuration
public class CustomRequestMappingHandlerMapping extends RequestMappingHandlerMapping{
	
	private static Logger logger = LoggerFactory.getLogger(CustomRequestMappingHandlerMapping.class);
	
	//private final static Map<HandlerMethod, RequestMappingInfo> HANDLER_METHOD_REQUEST_MAPPING_INFO_MAP = new HashMap<>();
	
	private static Map<String, HandlerMethod> NAME_HANDLER_MAP = new HashMap<String, HandlerMethod>();
    private static Map<HandlerMethod, RequestMappingInfo> MAPPING_HANDLER_MAP = new HashMap<HandlerMethod, RequestMappingInfo>();
    
	//初始化注册handlerMapping
	@Override
    protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
        //HandlerMethod handlerMethod = super.createHandlerMethod(handler, method);
        //HANDLER_METHOD_REQUEST_MAPPING_INFO_MAP.put(handlerMethod, mapping);
        //super.registerHandlerMethod(handler, method, mapping);
        
		HandlerMethod handlerMethod = super.createHandlerMethod(handler, method);
        RequestMapping rMapping = AnnotationUtils.getAnnotation(method, RequestMapping.class);
        
        String rmName = rMapping.name();
        if(StringUtils.isNotBlank(rmName)){
        	HandlerMethod repetHM = NAME_HANDLER_MAP.get(rmName);
            if(repetHM != null){
            	try {
    				throw new MappingNameRepetException("register handler method name repet，name：" + rMapping.name()
    						+ "，method1：" + repetHM.toString() + "，method2：" + handlerMethod.toString());
    			} catch (MappingNameRepetException e) {
    				e.printStackTrace();
    			}
            	return;
            }
            
            logger.info("====================== register handler method，name：" + rmName + "，handlerMethod："
                    + handlerMethod.toString());
            
        	NAME_HANDLER_MAP.put(rmName, handlerMethod);
            MAPPING_HANDLER_MAP.put(handlerMethod, mapping);
        }
        
        super.registerHandlerMethod(handler, method, mapping);
    }
	
    @Override
    protected HandlerMethod lookupHandlerMethod(String lookupPath, HttpServletRequest request) throws Exception {
    	
        //String extendHanderMethodName = request.getParameter("handler_method");
        /*String extendHanderMethodName = request.getHeader("handler_method");
        
        logger.info("handler mapping - lookup：" + extendHanderMethodName);
        
    	if(StringUtils.isEmpty(extendHanderMethodName)){
    		logger.info("execute super。。。");
    		return super.lookupHandlerMethod(lookupPath, request);
    	}
	       
    	logger.info("execute self。。。");
    	
	    //如果带了, 则从Map(这个Map中的entry在后面介绍)中获取处理当前url的方法
        List<HandlerMethod> handlerMethods = super.getHandlerMethodsForMappingName(extendHanderMethodName);
        if(CollectionUtils.isEmpty(handlerMethods)) throw new ServiceException("没有找到指定的方法");
        if(handlerMethods.size() > 1) throw new ServiceException("存在多个匹配的方法");

        HandlerMethod handlerMethod = handlerMethods.get(0);
        
        //根据处理方法查找RequestMappingInfo, 用于解析路径url中的参数
        RequestMappingInfo requestMappingInfo = HANDLER_METHOD_REQUEST_MAPPING_INFO_MAP.get(handlerMethod);
        boolean flag = requestMappingInfo == null;
        
        logger.info("get requestMappingInfo is null：" + flag);
        
        if(!flag){
	        super.handleMatch(requestMappingInfo, lookupPath, request);
	        return handlerMethod;
        }
        return super.lookupHandlerMethod(lookupPath, request);*/
    	
    	
    	String extendHanderMethodName = request.getHeader("handler_method");
        HandlerMethod handlerMethod = NAME_HANDLER_MAP.get(extendHanderMethodName);
        if (StringUtils.isNotBlank(extendHanderMethodName) && handlerMethod != null) {
            super.handleMatch(MAPPING_HANDLER_MAP.get(handlerMethod), lookupPath, request);
            return handlerMethod;
        }
        return super.lookupHandlerMethod(lookupPath, request);
    }
}
