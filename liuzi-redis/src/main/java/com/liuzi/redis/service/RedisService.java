package com.liuzi.redis.service;

import java.util.List;
import java.util.concurrent.TimeUnit;


public interface RedisService extends ZSetService{
	
	/**
     * 指定缓存失效时间
     * @param key 键
     * @param time 时间(秒)
     * @return
     */
    boolean expire(String key, long time);
    
    /**
     * 指定缓存失效时间
     * @param key 键
     * @param time 时间
     * @param timeUnit 时间单位
     * @return
     */
    boolean expire(String key, long time, TimeUnit timeUnit);
    
    /**
     * 根据key获取过期时间
     * @param key 键
     * @return 时间(秒) 返回0代表为永久有效
     */
    long getExpire(String key);
    
    /**
     * 判断key是否存在
     * @param key 键
     * @return true 存在 false不存在
     */
    boolean hasKey(String key);
	
	/**
     * 删除缓存
     * @param key 可以传一个值 或多个
     */
    void del(String... key);
    
    /**
	 * 分页
	 * @param key
	 * @param order
	 * @param desc
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	<T> List<T> page(String key, String order, boolean desc, 
			Integer pageNo, Integer pageSize);
}
