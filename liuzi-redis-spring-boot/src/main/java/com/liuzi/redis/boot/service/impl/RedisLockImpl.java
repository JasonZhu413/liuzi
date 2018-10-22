package com.liuzi.redis.boot.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import com.liuzi.redis.boot.service.RedisLock;

public class RedisLockImpl implements RedisLock{
	
	private static final String COMPARE_AND_DELETE =
            "if redis.call('get', KEYS[1]) == ARGV[1]\n " +
            "then\n    return redis.call('del',KEYS[1])\n " +
            "else\n    return 0\n end";
	
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
    @SuppressWarnings("unchecked")
	@Override
    public void unlock(){
        List<String> keys = Collections.singletonList(key);
        redisTemplate.execute(new DefaultRedisScript(COMPARE_AND_DELETE, String.class), 
        		keys, value);
    }

    @Override
    public void close() throws Exception {
        this.unlock();
    }
}
