package com.liuzi.push.umPush;



import lombok.Getter;
import lombok.Setter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import push.PushClient;
import push.android.AndroidBroadcast;
import push.android.AndroidCustomizedcast;
import push.ios.IOSBroadcast;
import push.ios.IOSCustomizedcast;



@Configuration
public class UMPushConfig{
	
	private static Logger logger = LoggerFactory.getLogger(UMPushConfig.class);
	
	private final static boolean DEFAULT_PRODUCTION = false;
	
	//key
	@Getter @Setter private String key; 
	//秘钥
	@Getter @Setter private String secret;
	//是否生产
	public static boolean production = DEFAULT_PRODUCTION;
	
	public UMPushConfig(boolean production){
		UMPushConfig.production = production;
	}
	
	@Bean
	public PushClient pushClient(){
		return new PushClient();
	}
	
	@Bean
	public IOSBroadcast iOSBroadcast() throws Exception{
		return new IOSBroadcast(key, secret);
	}
	
	@Bean
	public IOSCustomizedcast iOSCustomizedcast() throws Exception{
		return new IOSCustomizedcast(key, secret);
	}
	
	@Bean
	public AndroidBroadcast androidBroadcast() throws Exception{
		return new AndroidBroadcast(key, secret);
	}
	
	@Bean
	public AndroidCustomizedcast androidCustomizedcast() throws Exception{
		return new AndroidCustomizedcast(key, secret);
	}
	
	
}
