package com.liuzi.push.iosPush;


import javapns.communication.exceptions.KeystoreException;
import javapns.notification.AppleNotificationServer;
import javapns.notification.AppleNotificationServerBasicImpl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Getter
@Setter
@Configuration
public class IOSPushConfig {
	
	//ios证书路径
	private String key;
	//ios证书密码
	private String password;
	//ios是否正式环境(true正式，false开发)
	private boolean production = false;
	
	@Bean
	public AppleNotificationServer appleNotificationServer() throws KeystoreException{
		return new AppleNotificationServerBasicImpl(key, password, production);
	}
}
