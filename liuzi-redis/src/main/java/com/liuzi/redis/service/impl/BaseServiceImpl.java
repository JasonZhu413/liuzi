package com.liuzi.redis.service.impl;


import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ClusterOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.HyperLogLogOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class BaseServiceImpl{
	
	@Autowired
	protected RedisTemplate<String, Object> redisTemplate;
	
	@Bean
    public HashOperations<String, String, Object> hashOperations() {
    	return redisTemplate.opsForHash();
    }

    @Bean
    public ValueOperations<String, Object> valueOperations() {
    	return redisTemplate.opsForValue();
    }

    @Bean
    public ListOperations<String, Object> listOperations() {
    	return redisTemplate.opsForList();
    }

    @Bean
    public SetOperations<String, Object> setOperations() {
    	return redisTemplate.opsForSet();
    }

    @Bean
    public ZSetOperations<String, Object> zSetOperations() {
    	return redisTemplate.opsForZSet();
    }
    
    @Bean
	public HyperLogLogOperations<String, Object> opsForHyperLogLog() {
    	return redisTemplate.opsForHyperLogLog();
	}
    
    @Bean
	public ClusterOperations<String, Object> opsForCluster() {
    	return redisTemplate.opsForCluster();
	}
    
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
    	return redisTemplate.getConnectionFactory();
    }
    
    @Bean
    public RedisConnection redisConnection() {
    	return redisTemplate.getConnectionFactory().getConnection();
    }
	
    protected void info(String msg){
    	log.info("[Redis] " + msg);
    }
    
    protected void warn(String msg){
    	log.warn("[Redis] " + msg);
    }
    
    protected void error(String msg){
    	log.error("[Redis] " + msg);
    }
    
    /**
     * 指定缓存失效时间
     * @param key 键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time) {
    	return expire(key, time, TimeUnit.SECONDS);
    }
    
    /**
     * 指定缓存失效时间
     * @param key 键
     * @param time 时间
     * @param timeUnit 时间单位
     * @return
     */
    public boolean expire(String key, long time, TimeUnit timeUnit) {
        try {
            if (time > 0) {
            	timeUnit = timeUnit == null ? TimeUnit.SECONDS : timeUnit;
            	info("expire, key: " + key + ", time: " + time + ", timeUnit: " + timeUnit);
                redisTemplate.expire(key, time, timeUnit);
            }
            return true;
        } catch (Exception e) {
        	error("expire, key: " + key + ", msg: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 根据key获取过期时间
     * @param key 键
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key){
    	info("getExpire, key: " + key);
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }
    
    /**
     * 判断key是否存在
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
        	info("hasKey, key: " + key);
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
        	error("hasKey, key: " + key + ", msg: " + e.getMessage());
            return false;
        }
    }
	
	/**
     * 删除缓存
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
    	if (key == null || key.length == 0) {
    		return;
    	}
    	
    	try {
    		int lenth = key.length;
        	String[] keys = new String[lenth];
            if (lenth == 1) {
            	info("delete, key: " + keys[0]);
                redisTemplate.delete(keys[0]);
            } else {
            	List<String> list = CollectionUtils.arrayToList(keys);
            	info("delete, keys: " + list);
                redisTemplate.delete(list);
            }
        } catch (Exception e) {
        	error("delete, key: " + key + ", msg: " + e.getMessage());
        }
    	return;
    }
}
