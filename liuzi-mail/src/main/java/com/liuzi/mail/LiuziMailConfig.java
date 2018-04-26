package com.liuzi.mail;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.liuzi.util.LiuziUtil;

public class LiuziMailConfig{
	
	private static Logger logger = LoggerFactory.getLogger(LiuziMailConfig.class);
	
	private final static String DEFAULT_CONF_FILE_NAME = "conf/mail.properties";
	private final static boolean DEFAULT_USE_TEMP = false;
	private final static boolean DEFAULT_DEBUG = false;
	
	protected static String g_conf_file = DEFAULT_CONF_FILE_NAME;
	protected static boolean g_use_temp = DEFAULT_USE_TEMP;
	private static boolean g_debug = DEFAULT_DEBUG;
	protected volatile static VelocityEngine velocityEngine;
	
	protected static Properties properties;
	
	protected static MimeMessage message;
	protected static Session session;
	protected static Transport transport;
	protected static String smtpHost = "";
	protected static int smtpPort = 25;
	protected static String senderUserName = "";
	protected static String senderPassword = "";
	
	public LiuziMailConfig(){
		init();
	}
	
	public LiuziMailConfig(String confFile){
		init(confFile);
	}
	
	public LiuziMailConfig(String host, int port, String username, 
			String password, boolean auth, boolean debug, boolean useTemp){
		init(host, port, username, password, auth, debug, useTemp);
	}
	
	public static void init(String host, int port, String username, 
			String password, boolean auth, boolean debug, boolean useTemp) {
		properties = new Properties();
		properties.put("mail.smtp.host", smtpHost);
		properties.put("mail.smtp.port", smtpPort);
		properties.put("mail.sender.username", senderUserName);
		properties.put("mail.sender.password", senderPassword);
		properties.put("mail.smtp.auth", auth);
		properties.put("mail.code.debug", debug);
		properties.put("mail.use.temp", useTemp);
		init();
	}
	
	public static void init(String confFile) {
		if(!StringUtils.isEmpty(confFile)){
			g_conf_file = confFile;
		}
		init();
	}

	public static void init() {
		LiuziUtil.tag("  --------  Liuzi Mail初始化......  --------");
		
		logger.info("======== mail初始化，加载配置" + g_conf_file + " ========");
		
		if(properties == null){
			try (InputStream in = PropertyUtils.class.getClassLoader().getResourceAsStream(g_conf_file)){
				properties = new Properties();
				properties.load(in);
			} catch (IOException e) {
				logger.error("mail初始化失败，错误：" + e.getMessage());
				e.printStackTrace();
				return;
			}
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
			logger.info("创建session实例......");
			
			g_debug = Boolean.parseBoolean(properties.getProperty("mail.code.debug"));
			logger.info("加载参数：mail.code.debug：" + g_debug);
			
			session.setDebug(g_debug);//开启后有调试信息
			logger.info("是否开启调试（debug）：" + g_debug);
			
			message = new MimeMessage(session);
			logger.info("创建message实例......");
			
			g_use_temp = Boolean.parseBoolean(properties.getProperty("mail.use.temp"));
			
			if(g_use_temp) initVelocityEngine();
		} catch (Exception e) {
			logger.error("mail初始化失败，错误：" + e.getMessage());
			e.printStackTrace();
			return;
		}
	    
	    logger.info("======== mail初始化完成 ========\n");
	}
	
	private static void initVelocityEngine() throws Exception {
		logger.info("\n======== mail初始化 模板 配置...... ========");
		velocityEngine = new VelocityEngine();
		velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		velocityEngine.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_CACHE, false);
		velocityEngine.setProperty(RuntimeConstants.INPUT_ENCODING, "UTF-8");
		velocityEngine.setProperty(RuntimeConstants.OUTPUT_ENCODING, "UTF-8");
		velocityEngine.init();
		logger.info("======== mail模板 配置加载完成...... ========\n");
	}
}
