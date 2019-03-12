package com.liuzi.qmail;

import java.util.Properties;

import lombok.Data;

import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

@Data
class QmailConfig {
	
	private static final String TEMP_LOADER_PATH = "/";
	private static final int TEMP_UPDATE_DELAY = 0;
	private static final String FREEMARKER_DEFAULT_ENCODING = "UTF-8";
	private static final String FREEMARKER_LOCALE = "zh_CN";
	private static final boolean EMAIL_AUTH = true;
	
	private String host;
	private int port;
	private String username;
	private String password;
	private String from;
	private String subject;
	
	private boolean auth = EMAIL_AUTH;
	private String templateLoaderPath = TEMP_LOADER_PATH;
	private int templateUpdateDelay = TEMP_UPDATE_DELAY;
	private String defaultEncoding = FREEMARKER_DEFAULT_ENCODING;
	private String locale = FREEMARKER_LOCALE;
	
    @Bean
    public JavaMailSender javaMailSender(){
    	JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
    	javaMailSender.setHost(host);
    	javaMailSender.setPort(port);
    	javaMailSender.setUsername(username);
    	javaMailSender.setPassword(password);
    	
    	Properties p = new Properties();
    	p.setProperty("mail.sender.auth", auth + "");
    	javaMailSender.setJavaMailProperties(p);
    	return javaMailSender;
    }
    
    @Bean
    public SimpleMailMessage simpleMailMessage(){
    	SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    	simpleMailMessage.setFrom(from);
    	simpleMailMessage.setSubject(subject);
    	return simpleMailMessage;
    }
    
    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer(){
    	FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
    	freeMarkerConfigurer.setTemplateLoaderPath(templateLoaderPath);
    	
    	Properties settings = new Properties();
    	settings.setProperty("template_update_delay", templateUpdateDelay + "");
    	settings.setProperty("default_encoding", defaultEncoding);
    	settings.setProperty("locale", locale);
    	freeMarkerConfigurer.setFreemarkerSettings(settings);
    	
    	return freeMarkerConfigurer;
    }
}
