package com.liuzi.push.xiaomiPush;

import com.xiaomi.xmpush.server.Sender;

import org.springframework.context.annotation.Bean;



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
