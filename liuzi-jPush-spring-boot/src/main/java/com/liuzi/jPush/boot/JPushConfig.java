package com.liuzi.jPush.boot;




import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.liuzi.util.LiuziUtil;

import cn.jpush.api.JPushClient;


@Slf4j
@Configuration
public class JPushConfig{
	
	@Value("${jPush.app_key}")
	private String APP_KEY;
	@Value("${jPush.master_secret}")
	private String MASTER_SECRET;
	@Value("${jPush.alive_time}")
	public static long g_alive_time = 3 * 24 * 60 * 60; //过期时间默认3天 3 * 24 * 60 * 60
	@Value("${jPush.apns}")
	public static boolean g_apns = false; //默认不推送生产环境 false
	
	@Bean
    public JPushClient jpushClient(){
		LiuziUtil.tag("--------  Liuzi JPush初始化  --------");
		log.info("--------  Liuzi JPush初始化，注入jpushClient   --------");
		
		JPushClient jPushClient = new JPushClient(MASTER_SECRET, APP_KEY);
		
		log.info("--------  Liuzi JPush初始化完成   --------");
		return jPushClient;
    }

}
