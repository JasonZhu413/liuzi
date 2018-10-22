package com.liuzi.qmail.boot;


import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import lombok.extern.slf4j.Slf4j;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.liuzi.util.LiuziUtil;

@Slf4j
@Configuration
public class QMailConfig{
	
	public static Session session;
	public static Transport transport;
	
	@Value("${mail.smtp.host}")
	public static String smtpHost;
	@Value("${mail.smtp.port}")
	public static int smtpPort = 25;
	@Value("${mail.smtp.username}")
	public static String senderUserName;
	@Value("${mail.smtp.password}")
	public static String senderPassword;
	@Value("${mail.smtp.auth}")
	private static boolean auth;
	@Value("${mail.use.temp}")
	private static boolean g_use_temp;
	@Value("${mail.code.debug}")
	private static boolean g_debug;
	
	@Bean
	public MimeMessage mimeMessage() {
		LiuziUtil.tag("--------  Liuzi Mail初始化，注入mimeMessage  --------");
		
		Properties properties = new Properties();
		properties.setProperty("mail.smtp.host", smtpHost);
		properties.setProperty("mail.smtp.port", smtpPort + "");
		properties.setProperty("mail.sender.username", senderUserName);
		properties.setProperty("mail.sender.password", senderPassword);
		properties.setProperty("mail.smtp.auth", auth + "");
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
		log.info("--------  Liuzi Mail初始化，加载模板，注入velocityEngine："+g_use_temp+"--------");
		
		if(g_use_temp){
			velocityEngine = new VelocityEngine();
			velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
			velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
			velocityEngine.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_CACHE, false);
			velocityEngine.setProperty(RuntimeConstants.INPUT_ENCODING, "UTF-8");
			velocityEngine.setProperty(RuntimeConstants.OUTPUT_ENCODING, "UTF-8");
			velocityEngine.init();
		}
		return velocityEngine;
	}
}
