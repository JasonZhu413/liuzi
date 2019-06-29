package com.liuzi.qmail.boot;


import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import lombok.extern.slf4j.Slf4j;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.liuzi.util.common.LiuziUtil;


@Slf4j
@Configuration
public class QMailConfig{
	
	public static Session session;
	public static Transport transport;
	
	@Value("${mail.smtp.auth}")
	private boolean auth;
	@Value("${mail.transport.protocol}")
	private String protocol;
	@Value("${mail.use.temp}")
	private boolean g_use_temp;
	@Value("${mail.code.debug}")
	private boolean g_debug;
	
	@Bean
	public MimeMessage mimeMessage() {
		LiuziUtil.tag("--------  Liuzi Mail初始化 --------");
		log.info("--------  Liuzi Mail初始化，注入mimeMessage  --------");
		
		Properties properties = new Properties();
		//properties.setProperty("mail.smtp.host", smtpHost);
		//properties.setProperty("mail.smtp.port", smtpPort + "");
		//properties.setProperty("mail.sender.username", senderUserName);
		//properties.setProperty("mail.sender.password", senderPassword);
		properties.setProperty("mail.smtp.auth", auth + "");
		properties.setProperty("mail.transport.protocol", protocol);
	    try {
	    	session = Session.getInstance(properties);
	    	session.setDebug(g_debug);
	    	return new MimeMessage(session);
		} catch (Exception e) {
			log.error("mail初始化失败，错误：" + e.getMessage());
			e.printStackTrace();
		}
	    return null;
	}
	
	@Bean
	public VelocityEngine velocityEngine() {
		VelocityEngine velocityEngine = null;
		log.info("--------  Liuzi Mail初始化，加载模板，注入velocityEngine：" + g_use_temp+"--------");
		
		if(g_use_temp){
			velocityEngine = new VelocityEngine();
			velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
			velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
			velocityEngine.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_CACHE, false);
			velocityEngine.setProperty(RuntimeConstants.INPUT_ENCODING, "UTF-8");
			velocityEngine.setProperty(RuntimeConstants.OUTPUT_ENCODING, "UTF-8");
			velocityEngine.init();
		}
		
		log.info("--------  Liuzi Mail初始化完成  --------");
		return velocityEngine;
	}
}
