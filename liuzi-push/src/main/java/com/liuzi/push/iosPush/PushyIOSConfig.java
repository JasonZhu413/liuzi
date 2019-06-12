package com.liuzi.push.iosPush;


import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import java.io.File;

import org.springframework.context.annotation.Bean;

import com.turo.pushy.apns.ApnsClient;
import com.turo.pushy.apns.ApnsClientBuilder;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Data
public class PushyIOSConfig{
	//ios证书路径
	private String key;
	//ios证书密码
	private String password;
	//ios是否正式环境(true正式，false开发)
	private boolean production = false;
	
	public static String pkage;
	
	public PushyIOSConfig(String pkage){
		PushyIOSConfig.pkage = pkage;
	}
	
	@Bean
	public ApnsClient apnsClient(){
		ApnsClient apnsClient = null;
		try {
            EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);
            apnsClient = new ApnsClientBuilder()
            	.setApnsServer(production ? ApnsClientBuilder.PRODUCTION_APNS_HOST :
            		ApnsClientBuilder.DEVELOPMENT_APNS_HOST)
                .setClientCredentials(new File(key), password)
                .setConcurrentConnections(4)
                .setEventLoopGroup(eventLoopGroup)
                .build();
        } catch (Exception e) {
            log.error("ios get pushy apns client failed!");
            e.printStackTrace();
        }
		return apnsClient;
	}
}
