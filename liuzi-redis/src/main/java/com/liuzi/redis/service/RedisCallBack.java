package com.liuzi.redis.service;




public interface RedisCallBack {
	
	public void call(String key) throws Exception;
}
