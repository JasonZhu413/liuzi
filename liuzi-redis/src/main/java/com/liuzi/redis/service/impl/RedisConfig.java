package com.liuzi.redis.service.impl;


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
import org.springframework.stereotype.Component;



@Component
public class RedisConfig{
	
	protected final static long LOCK_TIME = 2000L;
	protected final static String LOCK_SUCCESS = "OK";
	protected final static String IF_NOT_EXIST = "NX";
	protected final static String WITH_EXPIRE_TIME = "PX";
	
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
}
