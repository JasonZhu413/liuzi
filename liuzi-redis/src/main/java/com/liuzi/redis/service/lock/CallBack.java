package com.liuzi.redis.service.lock;

public interface CallBack {
	
	public void call(String key) throws Exception;
}
