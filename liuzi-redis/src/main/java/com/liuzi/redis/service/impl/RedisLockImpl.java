package com.liuzi.redis.service.impl;

import java.util.Collections;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import redis.clients.jedis.Jedis;

import com.liuzi.redis.service.RedisLock;

public class RedisLockImpl implements RedisLock{
	
	private static final String COMPARE_AND_DELETE = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
	private static final Long RELEASE_SUCCESS = 1L;
	private RedisTemplate<String, Object> redisTemplate;
    private String key;
    private String value;

    protected RedisLockImpl(RedisTemplate<String, Object> redisTemplate, 
    		String key, String value){
        this.redisTemplate = redisTemplate;
        this.key = key;
        this.value = value;
    }

    /**
     * 释放redis分布式锁
     */
	@Override
    public void unlock(){
        redisTemplate.execute(new DefaultRedisScript<String>(COMPARE_AND_DELETE, String.class), 
        		Collections.singletonList(key), Collections.singletonList(value));
    }
	
	@Override
    public boolean release(){
        String result = redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
            	Jedis jedis = (Jedis) connection.getNativeConnection();
                Object obj = jedis.eval(COMPARE_AND_DELETE, Collections.singletonList(key), 
                		Collections.singletonList(value));
                return obj == null ? null : obj.toString();
            }
        });
        
        if (RELEASE_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }

    @Override
    public void close() throws Exception {
        this.unlock();
    }
}
