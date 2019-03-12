package com.liuzi.redis.boot;

import java.lang.reflect.Method;


















import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ClusterOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.HyperLogLogOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liuzi.util.common.LiuziUtil;


@Slf4j
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport{
	
	/**
	 * 管理缓存
	 */
	@Bean
	public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
		LiuziUtil.tag("--------  Liuzi Redis初始化 --------");
		RedisCacheManager redisCacheManager = RedisCacheManager.create(connectionFactory);
		return redisCacheManager;
	}
	
	/**
	 * redisTemplate 配置
	 * @param factory
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
		log.info("--------  Liuzi Redis初始化，注入redisTemplate  --------");
		
		StringRedisTemplate template = new StringRedisTemplate(factory);
		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
	}
	
	/**
	 * 生成key的策略
	 */
	@Bean
	public KeyGenerator keyGenerator() {
		log.info("--------  Liuzi Redis初始化，注入keyGenerator  --------");
		
		return new KeyGenerator() {
			@Override
			public Object generate(Object target, Method method, Object... params) {
				StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(obj.toString());
                }
                log.info("keyGenerator = " + sb.toString());
                return sb.toString();
			}
		};
	}

	
	/**
     * 对hash类型的数据操作
     * @param redisTemplate
     * @return
     */
    @Bean
    public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForHash();
    }

    /**
     * 对redis字符串类型数据操作
     * @param redisTemplate
     * @return
     */
    @Bean
    public ValueOperations<String, Object> valueOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    /**
     * 对链表类型的数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public ListOperations<String, Object> listOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForList();
    }

    /**
     * 对无序集合类型的数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public SetOperations<String, Object> setOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForSet();
    }

    /**
     * 对有序集合类型的数据操作
     * @param redisTemplate
     * @return
     */
    @Bean
    public ZSetOperations<String, Object> zSetOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForZSet();
    }
    
    @Bean
	public HyperLogLogOperations<String, Object> opsForHyperLogLog(RedisTemplate<String, Object> redisTemplate) {
		return redisTemplate.opsForHyperLogLog();
	}
    
    @Bean
	public ClusterOperations<String, Object> opsForCluster(RedisTemplate<String, Object> redisTemplate) {
		return redisTemplate.opsForCluster();
	}
    
    /**
     * RedisConnectionFactory
     * @param redisTemplate
     * @return
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.getConnectionFactory();
    }
    
    /**
     * RedisConnection
     * @param redisTemplate
     * @return
     */
    @Bean
    public RedisConnection redisConnection(RedisConnectionFactory redisConnectionFactory) {
        return redisConnectionFactory.getConnection();
    }
    
    
}
