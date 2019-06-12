package com.liuzi.redis.service;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

public class RedisConfig {
	
	/**
	 * host或ip
	 */
	@Getter @Setter
	private String hostName = "localhost";
	/**
	 * 端口号
	 */
	@Getter @Setter
	private int port = 6379;
	/**
	 * 密码
	 */
	@Getter @Setter
	private String password;
	/**
	 * 库
	 */
	@Getter @Setter
	private int database = 0;
	/**
	 * 开启事务
	 */
	@Getter @Setter
	private boolean enableTransactionSupport = false;
	
	@Autowired
	private JedisPoolConfig jedisPoolConfig;

	@Bean
    public RedisTemplate<String, Object> redisTemplate(@Qualifier("jedisConnectionFactory") 
    	JedisConnectionFactory jedisConnectionFactory){
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
        //Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        //ObjectMapper om = new ObjectMapper();
        //om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        //jackson2JsonRedisSerializer.setObjectMapper(om);
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(jdkSerializationRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(jdkSerializationRedisSerializer);
        redisTemplate.setEnableTransactionSupport(enableTransactionSupport);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
	
	/*@Bean
    public JedisConnectionFactory jedisConnectionFactory(@Qualifier("redisStandaloneConfiguration") 
    	RedisStandaloneConfiguration redisStandaloneConfiguration) {
        //获得默认的连接池构造器
        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpcb = 
                (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder)JedisClientConfiguration.builder();
        //指定jedisPoolConifig来修改默认的连接池构造器
        jpcb.poolConfig(jedisPoolConfig);
        //jedis连接工厂 = 单机配置 + 客户端配置
        return new JedisConnectionFactory(redisStandaloneConfiguration, jpcb.build());
    }
	
	@Bean("redisStandaloneConfiguration")
	public RedisStandaloneConfiguration redisStandaloneConfiguration(){
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(hostName);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
        //redisStandaloneConfiguration.setDatabase(database);
        return redisStandaloneConfiguration;
	}*/
}
