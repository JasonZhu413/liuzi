package com.liuzi.mail;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class MailConfig{
	
	private static Logger logger = LoggerFactory.getLogger(MailConfig.class);
	
	private final static String DEFAULT_CONF_FILE_NAME = "conf/mail.properties";
	private final static boolean DEFAULT_DEBUG = false;
	
	protected static String g_conf_file = DEFAULT_CONF_FILE_NAME;
	
	private static boolean debug = DEFAULT_DEBUG;
	
	protected static Properties properties;
	
	protected static MimeMessage message;
	protected static Session session;
	protected static Transport transport;
	protected static String smtpHost = "";
	protected static int smtpPort = 25;
	protected static String senderUserName = "";
	protected static String senderPassword = "";
	
	public MailConfig(String confFile){
		if(!StringUtils.isEmpty(confFile)){
			g_conf_file = confFile;
		}
		init();
	}

	public static void init() {
		logger.info("======== mail初始化，加载配置" + g_conf_file + " ========");
		
		try (InputStream in = PropertyUtils.class.getClassLoader().getResourceAsStream(g_conf_file)){
			properties = new Properties();
			properties.load(in);
		} catch (IOException e) {
			logger.error("mail初始化失败，错误：" + e.getMessage());
			e.printStackTrace();
			return;
		}
		
	    try {
			smtpHost = properties.getProperty("mail.smtp.host");
			logger.info("加载参数：mail.smtp.host：" + smtpHost);
			
			smtpPort = Integer.parseInt(properties.getProperty("mail.smtp.port"));
			logger.info("加载参数：mail.smtp.port：" + smtpPort);
			
			senderUserName = properties.getProperty("mail.sender.username");
			logger.info("加载参数：mail.sender.username：" + senderUserName);
			
			senderPassword = properties.getProperty("mail.sender.password");
			logger.info("加载参数：mail.sender.password：" + senderPassword);
			
			session = Session.getInstance(properties);
			logger.info("创建session实例：Session.getInstance(properties)");
			
			debug = Boolean.parseBoolean(properties.getProperty("mail.code.debug"));
			logger.info("加载参数：mail.code.debug：" + debug);
			
			session.setDebug(debug);//开启后有调试信息
			logger.info("是否开启调试（debug）：" + debug);
			
			message = new MimeMessage(session);
			logger.info("创建message实例：new MimeMessage(session)");
		} catch (NumberFormatException e) {
			logger.error("mail初始化失败，错误：" + e.getMessage());
			e.printStackTrace();
			return;
		}
	    
	    logger.info("======== mail初始化完成 ========");
	}
}
