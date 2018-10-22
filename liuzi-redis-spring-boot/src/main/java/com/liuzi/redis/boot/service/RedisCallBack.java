package com.liuzi.redis.boot.service;




public interface RedisCallBack {
	
	public void call(String key) throws Exception;
}
