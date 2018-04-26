package com.liuzi.redis;

import java.io.IOException;  
import java.io.InputStream;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

import com.liuzi.util.LiuziUtil;

import redis.clients.jedis.JedisPoolConfig;


  
@Slf4j
@Configuration
public class RedisConfig {
	private static Logger logger = LoggerFactory.getLogger(RedisConfig.class);
	
	private final static String CONFIG_FILE = "conf/redis.properties";
	private static String g_conf_file = CONFIG_FILE;
	
	protected static JedisPoolConfig jedisPoolConfig;
	public static JedisConnectionFactory jedisConnectionFactory;
	
	public static RedisTemplate<String, Object> redisTemplate;
	
	@Bean
    public RedisTemplate<String, Object> redisTemplate(){
		logger.info("redisTemplate 注入:" + (redisTemplate != null));
		return redisTemplate;
    }
	
	
	protected static Properties properties;
    
    public RedisConfig(){
    	init();
    }
    
    public RedisConfig(String confFile) {
    	if(!StringUtils.isEmpty(confFile)){
			g_conf_file = confFile;
		}
    	init();
    } 
	
	public static void init(){
		LiuziUtil.tag("  --------  Liuzi Redis初始化......  --------");
		
		log.info("===== redis初始化，加载配置 " + g_conf_file + " ......========");
		
		if(properties == null){
			try (InputStream in = PropertyUtils.class.getClassLoader().getResourceAsStream(g_conf_file)){
				properties = new Properties();
				properties.load(in);
			} catch (IOException e) {
				log.error("redis初始化失败，错误：" + e.getMessage());
				e.printStackTrace();
				return;
			}
		}
		
		String host = properties.getProperty("redis.host");
    	String port = properties.getProperty("redis.port");
    	String password = properties.getProperty("redis.password");
    	String timeout = properties.getProperty("redis.timeout");
    	
    	String maxIdle = properties.getProperty("redis.maxIdle");
    	String maxTotal = properties.getProperty("redis.maxTotal");
    	String maxWaitMillis = properties.getProperty("redis.maxWaitMillis");
    	String blockWhenExhausted = properties.getProperty("redis.blockWhenExhausted");
    	String testOnBorrow = properties.getProperty("redis.testOnBorrow");
    	String enableTransactionSupport = properties.getProperty("redis.enableTransactionSupport");
    	
    	jedisPoolConfig = new JedisPoolConfig();
    	jedisPoolConfig.setMaxIdle(Integer.parseInt(maxIdle));
    	jedisPoolConfig.setMaxTotal(Integer.parseInt(maxTotal));
    	jedisPoolConfig.setMaxWaitMillis(Integer.parseInt(maxWaitMillis));
    	jedisPoolConfig.setBlockWhenExhausted(Boolean.parseBoolean(blockWhenExhausted));
    	jedisPoolConfig.setTestOnBorrow(Boolean.parseBoolean(testOnBorrow));
    	
    	jedisConnectionFactory = new JedisConnectionFactory(jedisPoolConfig);
    	jedisConnectionFactory.setHostName(host);
    	jedisConnectionFactory.setPort(Integer.parseInt(port));
    	if(!StringUtils.isEmpty(password) && !"\"\"".equals(password)){
    		jedisConnectionFactory.setPassword(password);
    	}
    	jedisConnectionFactory.setTimeout(Integer.parseInt(timeout));
    	jedisConnectionFactory.setUsePool(true);
    	jedisConnectionFactory.afterPropertiesSet();
    	
    	redisTemplate = new RedisTemplate<>();
    	redisTemplate.setConnectionFactory(jedisConnectionFactory);
    	redisTemplate.setKeySerializer(new StringRedisSerializer());
    	redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
    	redisTemplate.setHashKeySerializer(new StringRedisSerializer());
    	redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
    	redisTemplate.setEnableTransactionSupport(Boolean.parseBoolean(enableTransactionSupport));
    	redisTemplate.afterPropertiesSet();
    	log.info("===== redis初始化完成 ......========");
	}
  
}  
