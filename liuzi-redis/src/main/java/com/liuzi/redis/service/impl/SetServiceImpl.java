package com.liuzi.redis.service.impl;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.liuzi.redis.service.SetService;
import com.liuzi.util.common.Log;


public class SetServiceImpl extends LockServiceImpl implements SetService{
	
    /**
     * set members获取Set中的所有值
     * @param key 键
     * @return
     */
    public Set<Object> sMem(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
        	Log.error(e, "set members error, key: {}", key);
            return null;
        }
    }

    /**
     * set isMember根据value从一个set中查询,是否存在
     * @param key 键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sIsMember(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
        	Log.error(e, "set isMember error, key: {}");
            return false;
        }
    }

    /**
     * set add将数据放入set缓存
     * @param key 键
     * @param values 值（可多个）
     * @return 成功个数
     */
    public long sAdd(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
        	Log.error(e, "set add error, key: {}", key);
            return 0;
        }
    }

    /**
     * set add数据放入缓存,并设置整个Set时间
     * @param key 键
     * @param time 时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sAdd(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            expire(key, time);
            return count;
        } catch (Exception e) {
        	Log.error(e, "set add error, key: {}", key);
            return 0;
        }
    }
    
    /**
     * set add数据放入缓存,并设置整个Set时间
     * @param key 键
     * @param time 时间
     * @param timeUnit 时间单位
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sAdd(String key, long time, TimeUnit timeUnit, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            expire(key, time, timeUnit);
            return count;
        } catch (Exception e) {
        	Log.error(e, "set add error, key: {}", key);
            return 0;
        }
    }

    /**
     * set size获取set缓存的长度
     * @param key 键
     * @return
     */
    public long sSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
        	Log.error(e, "set size error, key: {}", key);
            return 0;
        }
    }

    /**
     * 移除值为value的
     * @param key 键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long sRemove(String key, Object... values) {
        try {
        	return redisTemplate.opsForSet().remove(key, values);
        } catch (Exception e) {
        	Log.error(e, "set remove error, key: {}", key);
            return 0;
        }
    }
}
