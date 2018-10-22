package com.liuzi.rest.service;


public interface RestService {
	
	public String get(String url);
	
	public String get(String url, Object params);
	
	public <T> T get(String url, Object params, Class<T> clazz);
	
	public String post(String url);
	
	public String post(String url, Object params);
	
	public <T> T post(String url, Object params, Class<T> clazz);
}
