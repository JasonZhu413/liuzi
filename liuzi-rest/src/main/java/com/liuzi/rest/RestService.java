package com.liuzi.rest;


import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;



@Slf4j
@Service("restService")
public class RestService{
	
	@Autowired
	private RestTemplate restHttpTemplate;
	@Autowired
	private RestTemplate restHttpsTemplate;
	
	/**
	 * HTTP GET请求
	 * @param url 地址
	 * @return
	 */
	public String get(String url){
		return get(url, null, false);
	}
	
	/**
	 * HTTP GET请求
	 * @param url 地址
	 * @param obj 参数
	 * @return
	 */
	public String get(String url, Object obj){
		return get(url, obj, false);
	}
	
	/**
	 * HTTP/HTTPS GET请求
	 * @param url 地址
	 * @param isSSL 是否为https请求 true是 false否
	 * @return
	 */
	public String get(String url, boolean isSSL){
		return get(url, null, isSSL);
	}
	
	/**
	 * HTTP/HTTPS GET请求
	 * @param url 地址
	 * @param obj 参数
	 * @param isSSL 是否为https请求 true是 false否
	 * @return
	 */
	public String get(String url, Object obj, boolean isSSL){
		if(StringUtils.isEmpty(url)){
			log.warn("url is null");
			return null;
		}
		log.info("rest - get params：{}", obj);
		 
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept-Charset", "utf-8");
        headers.set("Content-type", "application/json;charset=utf-8");
        
        HttpEntity<Object> httpEntity;
        if(!StringUtils.isEmpty(obj)){
        	httpEntity = new HttpEntity<>(obj, headers);
        }else{
        	httpEntity = new HttpEntity<>(headers);
        }
        
        ResponseEntity<String> response;
        if(isSSL){
        	response = restHttpsTemplate.getForEntity(url, String.class, httpEntity);
        }else{
        	response = restHttpTemplate.getForEntity(url, String.class, httpEntity);
        }
        HttpStatus hs = response.getStatusCode();
        if(hs != HttpStatus.OK){
        	log.warn("error_code：{}", hs);
        	return null;
        }
        log.info("rest - success");
        return response.getBody();
	}
	
	/**
	 * HTTP POST请求
	 * @param url 地址
	 * @return
	 */
	public String post(String url){
		return post(url, null, false);
	}
	
	/**
	 * HTTP POST请求
	 * @param url 地址
	 * @param obj 参数
	 * @return
	 */
	public String post(String url, Object obj){
		return post(url, obj, false);
	}
	
	/**
	 * HTTP/HTTPS POST请求
	 * @param url 地址
	 * @param isSSL 是否为https请求 true是 false否
	 * @return
	 */
	public String post(String url, boolean isSSL){
		return post(url, null, isSSL);
	}
	
	/**
	 * HTTP/HTTPS POST请求
	 * @param url 地址
	 * @param obj 参数
	 * @param isSSL 是否为https请求 true是 false否
	 * @return
	 */
	public String post(String url, Object obj, boolean isSSL){
		if(StringUtils.isEmpty(url)){
			log.warn("url is null");
			return null;
		}
		
		log.info("rest - post params：{}", obj);
		 
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept-Charset", "utf-8");
        headers.set("Content-type", "application/json;charset=utf-8");
        
        HttpEntity<Object> httpEntity;
        if(!StringUtils.isEmpty(obj)){
        	httpEntity = new HttpEntity<>(obj, headers);
        }else{
        	httpEntity = new HttpEntity<>(headers);
        }
        
        ResponseEntity<String> response;
        if(isSSL){
        	response = restHttpsTemplate.postForEntity(url, httpEntity, String.class);
        }else{
        	response = restHttpTemplate.postForEntity(url, httpEntity, String.class);
        }
        HttpStatus hs = response.getStatusCode();
        if(hs != HttpStatus.OK){
        	log.warn("error_code：{}", hs);
        	return null;
        }
        log.info("rest - success");
        return response.getBody();
	}
	
}
