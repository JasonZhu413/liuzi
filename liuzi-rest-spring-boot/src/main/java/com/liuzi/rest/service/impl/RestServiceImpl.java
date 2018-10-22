package com.liuzi.rest.service.impl;


import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.liuzi.rest.service.RestService;



@Service("restService")
public class RestServiceImpl implements RestService {
	
	private static Logger logger = LoggerFactory.getLogger(RestServiceImpl.class);
	
	@Resource
	private RestTemplate restTemplate;
	
	@Override
	public String get(String url){
		return get(url, null);
	}
	
	@Override
	public String get(String url, Object obj){
		return _get(url, obj, null).toString();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(String url, Object obj, Class<T> clazz){
		return (T) _get(url, obj, clazz);
	}
	
	@Override
	public String post(String url){
		return post(url, null);
	}
	
	@Override
	public String post(String url, Object obj){
		return _post(url, obj, null).toString();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T post(String url, Object obj, Class<T> clazz){
		return (T) _post(url, obj, clazz);
	}
	
	
	private <T> Object _get(String url, Object obj, Class<T> clazz){
		logger.info("rest - get params：{}", obj);
		 
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept-Charset", "utf-8");
        headers.set("Content-type", "application/json;charset=utf-8");
        
        HttpEntity<Object> httpEntity;
        if(!StringUtils.isEmpty(obj)){
        	httpEntity = new HttpEntity<>(obj, headers);
        }else{
        	httpEntity = new HttpEntity<>(headers);
        }
        
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, httpEntity);
        HttpStatus hs = response.getStatusCode();
        if(hs != HttpStatus.OK){
        	logger.warn("error_code：{}", hs);
        	return null;
        }
        
        String body = response.getBody();
        if(clazz != null){
        	T t = JSONObject.parseObject(body, clazz);
        	logger.info("rest - success：", t);
        	return t;
        }
        
        logger.info("rest - success：", body);
		return body;
	}
	
	private <T> Object _post(String url, Object obj, Class<T> clazz){
		logger.info("rest - post params：{}", obj);
		 
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept-Charset", "utf-8");
        headers.set("Content-type", "application/json;charset=utf-8");
        
        HttpEntity<Object> httpEntity;
        if(!StringUtils.isEmpty(obj)){
        	httpEntity = new HttpEntity<>(obj, headers);
        }else{
        	httpEntity = new HttpEntity<>(headers);
        }
        
        ResponseEntity<String> response = restTemplate.postForEntity(url, httpEntity, String.class);
        HttpStatus hs = response.getStatusCode();
        if(hs != HttpStatus.OK){
        	logger.warn("error_code：{}", hs);
        	return null;
        }
        
        String body = response.getBody();
        if(clazz != null){
        	T t = JSONObject.parseObject(body, clazz);
        	logger.info("rest - success：", t);
        	return t;
        }
        
        logger.info("rest - success：", body);
		return body;
	}
	
}
