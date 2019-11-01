package com.liuzi.redis.service.impl;


import java.util.concurrent.TimeUnit;

import com.liuzi.redis.service.StringService;
import com.liuzi.util.common.Log;


public class StringServiceImpl extends SetServiceImpl implements StringService{
	
    /**
     * string get获取
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * string set放入
     * @param key 键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
        	Log.error(e, "string set error, key: {}", key);
        }
        return false;
    }

    /**
     * string set time放入并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒) 若小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        return set(key, value, time, TimeUnit.SECONDS);
    }
    
    /**
     * string set time放入并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒) 若小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time, TimeUnit timeUnit) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, timeUnit);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
        	Log.error(e, "string set time error, key: {}", key);
            return false;
        }
    }
    
    /**
     * string increment递增
     * @param key 键
     * @return
     */
    public long incr(String key) {
        return incr(key, 1);
    }

    /**
     * string increment delta递增
     * @param key 键
     * @param delta 递增因子(大于0)
     * @return
     */
    public long incr(String key, long delta) {
        if (delta <= 0) {
        	Log.error("string increment error, key: {}, msg: 递增因子必须大于0", key);
            throw new IllegalArgumentException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }
    
    /**
     * string decrement递减
     * @param key 键
     * @return
     */
    public long decr(String key) {
        return decr(key, 1);
    }

    /**
     * string decrement delta递减
     * @param key 键
     * @param delta 递增因子(大于0)
     * @return
     */
    public long decr(String key, long delta) {
        if (delta <= 0) {
        	Log.error("string decrement error, key: {}, msg: 递减因子必须大于0", key);
            throw new IllegalArgumentException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

}
