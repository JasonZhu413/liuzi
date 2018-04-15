package com.liuzi.qsms;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class QSmsConfig{
	
	private static Logger logger = LoggerFactory.getLogger(QSmsConfig.class);
	
	private final static String DEFAULT_CONF_FILE_NAME = "conf/qsms.properties";
	
	protected static Properties properties;
	protected static String g_conf_file = DEFAULT_CONF_FILE_NAME;
	protected static int app_id;
	protected static String app_key;
	
	public QSmsConfig(String confFile){
		if(!StringUtils.isEmpty(confFile)){
			g_conf_file = confFile;
		}
		init();
	}

	public static void init() {
		logger.info("======== qsms初始化，加载配置" + g_conf_file + " ========");
		
		try (InputStream in = PropertyUtils.class.getClassLoader().getResourceAsStream(g_conf_file)){
			properties = new Properties();
			properties.load(in);
		} catch (IOException e) {
			logger.error("qsms初始化失败，错误：" + e.getMessage());
			e.printStackTrace();
			return;
		}
		
		try {
			app_id = Integer.parseInt(properties.getProperty("sms.app.id"));
			logger.info("加载参数：sms.app.id：" + app_id);
			
			app_key = properties.getProperty("sms.app.key");
			logger.info("加载参数：sms.app.key：" + app_key);
		} catch (NumberFormatException e) {
			logger.error("qsms初始化失败，错误：" + e.getMessage());
			e.printStackTrace();
			return;
		}
		
		logger.info("======== qsms初始化完成 ========");
	}
}
