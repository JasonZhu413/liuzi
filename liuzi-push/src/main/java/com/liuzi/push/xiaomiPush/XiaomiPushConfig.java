package com.liuzi.push.xiaomiPush;

import com.xiaomi.xmpush.server.Sender;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;



@Slf4j
public class XiaomiPushConfig {
	
	private String secret_android;
	private String secret_ios;
	protected static String package_name;
 
	public XiaomiPushConfig(String packageName){
		XiaomiPushConfig.package_name = packageName;
	}
	
	@Bean
	public Sender xiaoMiSenderIOS(){
		//ios推送
	    return new Sender(secret_ios);
	}
	
	@Bean
	public Sender xiaoMiSenderAndroid(){
		//android推送
	    return new Sender(secret_android);
	}
}
