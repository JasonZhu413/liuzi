package com.liuzi.push.jgPush;



import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;

import cn.jpush.api.JPushClient;


@Data
@Slf4j
public class JGPushConfig{
	
	private final static long DEFAULT_ALIVETIME = 24 * 60 * 60;
	private final static boolean DEFAULT_PRODUCTION = false;
	
	//key
	private String key;
	//秘钥
	private String secret;
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
		log.info("JPushClient初始化, masterSecret:" + secret + ", appKey:" + key);
		return new JPushClient(secret, key);
    }
}
