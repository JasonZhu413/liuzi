package com.liuzi.qsms.boot;


import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsStatusPuller;
import com.github.qcloudsms.SmsVoicePromptSender;
import com.github.qcloudsms.SmsVoiceVerifyCodeSender;
import com.liuzi.util.common.LiuziUtil;

@Slf4j
@Configuration
public class QSmsConfig{

	@Value("${sms.app.id}")
	private int app_id;
	@Value("${sms.app.key}")
	private String app_key;
	
	@Bean
	public SmsSingleSender smsSingleSender() throws Exception{
		LiuziUtil.tag("--------  Liuzi QSms初始化 --------");
		log.info("--------  Liuzi QSms初始化，注入smsSingleSender  --------");
		return new SmsSingleSender(app_id, app_key);
	}
	
	@Bean
	public SmsMultiSender smsMultiSender() throws Exception{
		log.info("--------  Liuzi QSms初始化，注入smsMultiSender  --------");
		return new SmsMultiSender(app_id, app_key);
	}
	
	@Bean
	public SmsVoiceVerifyCodeSender smsVoiceVerifyCodeSender() throws Exception{
		log.info("--------  Liuzi QSms初始化，注入smsVoiceVerifyCodeSender  --------");
		return new SmsVoiceVerifyCodeSender(app_id, app_key);
	}
	
	@Bean
	public SmsVoicePromptSender smsVoicePromptSender() throws Exception{
		log.info("--------  Liuzi QSms初始化，注入smsVoicePromptSender  --------");
		return new SmsVoicePromptSender(app_id, app_key);
	}
	
	@Bean
	public SmsStatusPuller smsStatusPuller() throws Exception{
		log.info("--------  Liuzi QSms初始化，注入smsStatusPuller  --------");
		log.info("--------  Liuzi QSms初始化完成  --------");
		return new SmsStatusPuller(app_id, app_key);
	}
}
