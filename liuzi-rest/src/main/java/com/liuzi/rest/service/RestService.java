package com.liuzi.rest.service;




/**
 * rest接口
 * @author zsy
 */
public interface RestService{
	
	/**
	 * HTTP GET请求
	 * @param url 地址
	 */
	public String httpGet(String url);
	
	/**
	 * HTTP GET请求
	 * @param url 地址
	 * @param obj 参数
	 */
	public String httpGet(String url, Object obj);
	
	/**
	 * HTTP GET请求
	 * @param url 地址
	 * @param obj 参数(application/json)
	 */
	public String httpGetByJson(String url, Object obj);
	
	/**
	 * HTTPS GET请求
	 * @param url 地址
	 */
	public String httpsGet(String url);
	
	/**
	 * HTTPS GET请求
	 * @param url 地址
	 * @param obj 参数
	 */
	public String httpsGet(String url, Object obj);
	
	/**
	 * HTTPS GET请求
	 * @param url 地址
	 * @param obj 参数(application/json)
	 */
	public String httpsGetByJson(String url, Object obj);
	
	/**
	 * HTTP POST请求
	 * @param url 地址
	 */
	public String httpPost(String url);
	
	/**
	 * HTTP POST请求
	 * @param url 地址
	 * @param obj 参数
	 */
	public String httpPost(String url, Object obj);
	
	/**
	 * HTTP POST请求
	 * @param url 地址
	 * @param obj 参数(application/json)
	 */
	public String httpPostByJson(String url, Object obj);
	
	/**
	 * HTTPS POST请求
	 * @param url 地址
	 */
	public String httpsPost(String url);
	
	/**
	 * HTTPS POST请求
	 * @param url 地址
	 * @param obj 参数
	 */
	public String httpsPost(String url, Object obj);
	
	/**
	 * HTTPS POST请求
	 * @param url 地址
	 * @param obj 参数(application/json)
	 */
	public String httpsPostByJson(String url, Object obj);
}
