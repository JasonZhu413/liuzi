package com.liuzi.rest.service.impl;


import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.liuzi.rest.service.RestService;



@Slf4j
@Service("restService")
public class RestServiceImpl implements RestService{
	
	@Autowired
	private RestTemplate restHttpTemplate;
	@Autowired
	private RestTemplate restHttpsTemplate;
	
	/**
	 * HTTP GET请求
	 * @param url 地址
	 * @return
	 */
	public String httpGet(String url){
		return doGet(url, null, false, false, false);
	}
	
	/**
	 * HTTP GET请求
	 * @param url 地址
	 * @param obj 参数
	 * @return
	 */
	public String httpGet(String url, Object obj){
		return doGet(url, obj, false, false, false);
	}
	
	/**
	 * HTTP GET请求
	 * @param url 地址
	 * @param obj 参数
	 * @return
	 */
	public String httpGetByJson(String url, Object obj){
		return doGet(url, obj, true, false, false);
	}
	
	/**
	 * HTTP GET请求
	 * @param url 地址
	 * @return
	 */
	public String httpGetAsyn(String url){
		return doGet(url, null, false, false, true);
	}
	
	/**
	 * HTTP GET请求
	 * @param url 地址
	 * @param obj 参数
	 * @return
	 */
	public String httpGetAsyn(String url, Object obj){
		return doGet(url, obj, false, false, true);
	}
	
	/**
	 * HTTP GET请求
	 * @param url 地址
	 * @param obj 参数
	 * @return
	 */
	public String httpGetByJsonAsyn(String url, Object obj){
		return doGet(url, obj, true, false, true);
	}
	
	/**
	 * HTTPS GET请求
	 * @param url 地址
	 * @return
	 */
	public String httpsGet(String url){
		return doGet(url, null, false, true, false);
	}
	
	/**
	 * HTTPS GET请求
	 * @param url 地址
	 * @param obj 参数
	 * @return
	 */
	public String httpsGet(String url, Object obj){
		return doGet(url, obj, false, true, false);
	}
	
	/**
	 * HTTPS GET请求
	 * @param url 地址
	 * @param obj 参数
	 * @return
	 */
	public String httpsGetByJson(String url, Object obj){
		return doGet(url, obj, true, true, false);
	}
	
	/**
	 * HTTPS GET请求
	 * @param url 地址
	 * @param obj 参数
	 * @return
	 */
	public String httpsGetAsyn(String url){
		return doGet(url, null, false, true, true);
	}
	
	/**
	 * HTTPS GET请求
	 * @param url 地址
	 * @param obj 参数
	 * @return
	 */
	public String httpsGetAsyn(String url, Object obj){
		return doGet(url, obj, false, true, true);
	}
	
	/**
	 * HTTPS GET请求
	 * @param url 地址
	 * @param obj 参数
	 * @return
	 */
	public String httpsGetByJsonAsyn(String url, Object obj){
		return doGet(url, obj, true, true, true);
	}
	
	/**
	 * HTTP POST请求
	 * @param url 地址
	 * @return
	 */
	public String httpPost(String url){
		return doPost(url, false, false, false, false);
	}
	
	/**
	 * HTTP POST请求
	 * @param url 地址
	 * @param obj 参数
	 * @return
	 */
	public String httpPost(String url, Object obj){
		return doPost(url, obj, false, false, false);
	}
	
	/**
	 * HTTP POST请求
	 * @param url 地址
	 * @param obj 参数
	 * @return
	 */
	public String httpPostByJson(String url, Object obj){
		return doPost(url, obj, true, false, false);
	}
	
	/**
	 * HTTP POST请求
	 * @param url 地址
	 * @return
	 */
	public String httpPostAsyn(String url){
		return doPost(url, false, false, false, true);
	}
	
	/**
	 * HTTP POST请求
	 * @param url 地址
	 * @param obj 参数
	 * @return
	 */
	public String httpPostAsyn(String url, Object obj){
		return doPost(url, obj, false, false, true);
	}
	
	/**
	 * HTTP POST请求
	 * @param url 地址
	 * @param obj 参数
	 * @return
	 */
	public String httpPostByJsonAsyn(String url, Object obj){
		return doPost(url, obj, true, false, true);
	}
	
	/**
	 * HTTPS POST请求
	 * @param url 地址
	 * @return
	 */
	public String httpsPost(String url){
		return doPost(url, null, false, true, false);
	}
	
	/**
	 * HTTPS POST请求
	 * @param url 地址
	 * @param obj 参数
	 * @return
	 */
	public String httpsPost(String url, Object obj){
		return doPost(url, obj, false, true, false);
	}
	
	/**
	 * HTTPS POST请求
	 * @param url 地址
	 * @param obj 参数
	 * @return
	 */
	public String httpsPostByJson(String url, Object obj){
		return doPost(url, obj, true, true, false);
	}
	
	/**
	 * HTTPS POST请求
	 * @param url 地址
	 * @return
	 */
	public String httpsPostAsyn(String url){
		return doPost(url, null, false, true, true);
	}
	
	/**
	 * HTTPS POST请求
	 * @param url 地址
	 * @param obj 参数
	 * @return
	 */
	public String httpsPostAsyn(String url, Object obj){
		return doPost(url, obj, false, true, true);
	}
	
	/**
	 * HTTPS POST请求
	 * @param url 地址
	 * @param obj 参数
	 * @return
	 */
	public String httpsPostByJsonAsyn(String url, Object obj){
		return doPost(url, obj, true, true, true);
	}
	
	/**
	 * HTTP/HTTPS GET请求
	 * @param url 地址
	 * @param obj 参数
	 * @param isJson 请求参数是否为json true是 false否
	 * @param isSSL 是否为https请求 true是 false否
	 * @param isAsyn 是否为异步请求 true是 false否
	 * @return
	 */
	private String doGet(String url, Object obj, boolean isJson, boolean isSSL, boolean isAsyn){
		if(StringUtils.isEmpty(url)){
			log.error("[GET] Error, url is null, request end ...");
			return null;
		}
		
		log.info("[GET] Rest request start...");
		log.info("[GET] Params: {}", obj);
		
		String result = null;
		try {
			HttpHeaders headers = new HttpHeaders();
	        headers.set("Accept-Charset", "utf-8");
	        if(isJson){
	        	headers.set("Content-type", "application/json;charset=utf-8");
	        	
	        	log.info("[GET] Content-type: application/json;charset=utf-8");
	        }
	        
	        log.info("[GET] Init HttpEntity...");
	        
			HttpEntity<Object> httpEntity;
			if(!StringUtils.isEmpty(obj)){
				httpEntity = new HttpEntity<>(obj, headers);
			}else{
				httpEntity = new HttpEntity<>(headers);
			}
			
			log.info("[GET] Select resTemplate type, send request...");
			
			RestTemplate restTemplate = isSSL ? restHttpsTemplate : restHttpTemplate;
			ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, httpEntity);
			log.info("[GET] Response：{}", response);
			if(response == null){
				log.error("[GET] Error, response is null");
				return null;
			}
			
			result = response.getBody();
			HttpStatus hs = response.getStatusCode();
			if(hs != HttpStatus.OK){
				log.error("[GET] Error, HttpStatus wrong!");
			}
		} catch (RestClientException e) {
			e.printStackTrace();
			log.error("[GET] Error: " + e.getMessage());
		}
        
		log.info("[GET] Rest request end ...");
        return result;
	}
	
	/**
	 * HTTP/HTTPS POST请求
	 * @param url 地址
	 * @param obj 参数
	 * @param isJson 请求参数是否为json true是 false否
	 * @param isSSL 是否为https请求 true是 false否
	 * @param isAsyn 是否为异步请求 true是 false否
	 * @return
	 */
	public String doPost(String url, Object obj, boolean isJson, boolean isSSL, boolean isAsyn){
		if(StringUtils.isEmpty(url)){
			log.error("[POST] Error, url is null, request end ...");
			return null;
		}
		
		log.info("[POST] Rest request start ...");
		log.info("[POST] Params: {}", obj);
		
		String result = null;
		try {
	        HttpHeaders headers = new HttpHeaders();
	        headers.set("Accept-Charset", "utf-8");
	        if(isJson){
	        	headers.set("Content-type", "application/json;charset=utf-8");
	        	
	        	log.info("[POST] Content-type: application/json;charset=utf-8");
	        }
	        
	        log.info("[POST] Init HttpEntity...");
        
	        HttpEntity<Object> httpEntity;
	        if(!StringUtils.isEmpty(obj)){
	        	httpEntity = new HttpEntity<>(obj, headers);
	        }else{
	        	httpEntity = new HttpEntity<>(headers);
	        }
	        
	        log.info("[POST] Select resTemplate type, send request...");
        
	        /*if(obj instanceof String){
	        	httpEntity = new StringEntity(obj, CHARSET );
	        }*/
	        
	        RestTemplate restTemplate = isSSL ? restHttpsTemplate : restHttpTemplate;
			ResponseEntity<String> response = restTemplate.postForEntity(url, httpEntity, String.class);
			log.info("[POST] Response：{}", response);
			if(response == null){
				log.error("[POST] Error, response is null");
				return null;
			}
			
			result = response.getBody();
			HttpStatus hs = response.getStatusCode();
	        if(hs != HttpStatus.OK){
	        	log.error("[POST] Error, HttpStatus wrong!");
	        }
		} catch (RestClientException e) {
			e.printStackTrace();
			log.error("[POST] Error: " + e.getMessage());
		}
        
        log.info("[POST] Rest request end ...");
        return result;
	}
}
