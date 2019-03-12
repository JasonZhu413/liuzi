package com.liuzi.push.huaweiPush;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;

import com.geesanke.plugin.huawei.push.SendClient;


@Slf4j
public class HuaweiPushConfig {
	
	@Getter @Setter private String secret;
	@Getter @Setter private String id;
	
	@Bean
	public SendClient SendClient(){
		return new SendClient(secret, id);
	}
}
