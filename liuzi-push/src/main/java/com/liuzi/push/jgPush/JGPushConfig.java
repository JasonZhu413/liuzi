package com.liuzi.push.jgPush;



import lombok.Getter;
import lombok.Setter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.jpush.api.JPushClient;



@Configuration
public class JGPushConfig{
	
	private static Logger logger = LoggerFactory.getLogger(JGPushConfig.class);
	
	private final static long DEFAULT_ALIVETIME = 24 * 60 * 60;
	private final static boolean DEFAULT_PRODUCTION = false;
	
	//key
	@Getter @Setter private String key;
	//秘钥
	@Getter @Setter private String secret;
	//推送过期时间
	public static long aliveTime = DEFAULT_ALIVETIME; 
	//APNS推送环境是否生产
	public static boolean production = DEFAULT_PRODUCTION;
	
	public JGPushConfig(long aliveTime, boolean production){
		JGPushConfig.aliveTime = aliveTime;
		JGPushConfig.production = production;
	}
	
	@Bean
    public JPushClient jpushClient(){
		logger.info("JPushClient初始化, masterSecret:" + secret + ", appKey:" + key);
		JPushClient jpushClient = new JPushClient(secret, key);
		return jpushClient;
    }
}
