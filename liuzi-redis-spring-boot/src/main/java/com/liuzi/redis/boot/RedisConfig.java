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
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liuzi.util.LiuziUtil;


@Slf4j
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport{
	
	/**
	 * 管理缓存
	 */
	@Bean
	public CacheManager cacheManager(RedisTemplate redisTemplate) {
		LiuziUtil.tag("--------  Liuzi Redis初始化 --------");
		log.info("--------  Liuzi Redis初始化，注入cacheManager  --------");
		return new RedisCacheManager(redisTemplate);
	}
	
	/**
	 * redisTemplate 配置
	 * @param factory
	 * @return
	 */
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

}
