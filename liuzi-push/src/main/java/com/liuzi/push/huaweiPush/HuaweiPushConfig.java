package com.liuzi.push.huaweiPush;


import lombok.Data;

import org.springframework.context.annotation.Bean;

import com.geesanke.plugin.huawei.push.SendClient;

@Data
public class HuaweiPushConfig {
	
	private String secret;
	private String id;
	
	@Bean
	public SendClient SendClient(){
		return new SendClient(secret, id);
	}
}
