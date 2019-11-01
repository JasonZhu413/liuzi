package com.liuzi.redis.service.impl;


import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.liuzi.redis.service.HashService;
import com.liuzi.util.common.Log;


public class HashServiceImpl extends BaseServiceImpl implements HashService{
	
    /**
     * hash get获取
     * @param key 键
     * @param item 项
     * @return T
     */
    @SuppressWarnings("unchecked")
    public <T> T hGet(String key, String item) {
        return (T) redisTemplate.opsForHash().get(key, item);
    }

	/**
     * hash get获取所有
     * @param key 键
     * @return Map
     */
	public Map<Object, Object> hGet(String key) {
		return redisTemplate.opsForHash().entries(key);
	}

    /**
     * hash set设置多个
     * @param key 键
     * @param map 多个键值(项, 值)
     * @return true成功 false失败
     */
	public boolean hSet(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
        	Log.error(e, "hash set error, key: {}", key);
            return false;
        }
    }

    /**
     * hash set time设置多个并设置时间
     * @param key 键
     * @param map 对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hSet(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            expire(key, time);
            return true;
        } catch (Exception e) {
        	Log.error(e, "hash set, key: {}", key);
            return false;
        }
    }
    
    /**
     * hash set time设置多个并设置时间
     * @param key 键
     * @param map 对应多个键值
     * @param time 时间
     * @param timeUnit 时间单位
     * @return true成功 false失败
     */
    public boolean hSet(String key, Map<String, Object> map, long time, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            expire(key, time, timeUnit);
            return true;
        } catch (Exception e) {
        	Log.error(e, "hash set, key: {}", key);
            return false;
        }
    }

    /**
     * hash set设置
     * @param key 键
     * @param item 项
     * @param value 值
     * @return true成功 false失败
     */
    public boolean hSet(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
        	Log.error("hash set, key: {}", key);
            return false;
        }
    }

    /**
     * hash set time设置
     * @param key 键
     * @param item 项
     * @param value 值
     * @param time 时间(秒)
     * @return true成功  false失败
     */
    public boolean hSet(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            expire(key, time);
            return true;
        } catch (Exception e) {
        	Log.error(e, "hash set, key: {}", key);
            return false;
        }
    }
    
    /**
     * hash set time设置
     * @param key 键
     * @param item 项
     * @param value 值
     * @param time 时间
     * @param timeUnit 时间单位
     * @return true成功  false失败
     */
    public boolean hSet(String key, String item, Object value, long time, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            expire(key, time, timeUnit);
            return true;
        } catch (Exception e) {
        	Log.error(e, "hash set, key: {}", key);
            return false;
        }
    }

    /**
     * hash delete删除
     * @param key 键
     * @param item 项(可以多个)
     */
    public void hDel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * hash hasKey判断是否有该项的值
     * @param key 键
     * @param item 项
     * @return true存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }
    
    /**
     * hash increment delta递增
     * @param key 键
     * @param item 项
     * @param delta 递增因子(大于0)
     * @return
     */
    public double hIncr(String key, String item, long delta) {
    	if(delta <= 0){
    		Log.error("hash increment error, key: {}, msg: 递增因子必须大于0", key);
    		throw new IllegalArgumentException("递增因子必须大于0");
    	}
        return redisTemplate.opsForHash().increment(key, item, delta);
    }

    /**
     * hash increment delta递增
     * @param key 键
     * @param item 项
     * @param delta 递增因子(大于0)
     * @return
     */
    public double hIncr(String key, String item, double delta) {
    	if(delta <= 0){
    		Log.error("hash increment error, key: {}, msg: 递增因子必须大于0", key);
    		throw new IllegalArgumentException("递增因子必须大于0");
    	}
        return redisTemplate.opsForHash().increment(key, item, delta);
    }

    /**
     * hash decr delta递减
     * @param key 键
     * @param item 项
     * @param delta 递增因子(大于0)
     * @return
     */
    public double hDecr(String key, String item, double delta) {
    	if(delta <= 0){
    		Log.error("hash decr error, key: {}, msg: 递减因子必须大于0", key);
    		throw new IllegalArgumentException("递减因子必须大于0");
    	}
        return redisTemplate.opsForHash().increment(key, item, delta);
    }
    
    /**
     * hash decr delta递减
     * @param key 键
     * @param item 项
     * @param delta 递增因子(大于0)
     * @return
     */
    public double hDecr(String key, String item, long delta) {
    	if(delta <= 0){
    		Log.error("hash decr error, key: {}, msg: 递减因子必须大于0", key);
    		throw new IllegalArgumentException("递减因子必须大于0");
    	}
        return redisTemplate.opsForHash().increment(key, item, delta);
    }
}
