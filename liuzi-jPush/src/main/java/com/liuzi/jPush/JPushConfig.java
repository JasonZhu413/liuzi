package com.liuzi.jPush;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import cn.jpush.api.JPushClient;

import com.liuzi.util.LiuziUtil;


@Configuration
public class JPushConfig{
	
	private static Logger logger = LoggerFactory.getLogger(JPushConfig.class);
	
	private final static String DEFAULT_CONF_FILE_NAME = "conf/jPush.properties";
	private final static long ALIVE_TIME = 3 * 24 * 60 * 60;
	private final static boolean APNS = false;
	
	private static String g_conf_file = DEFAULT_CONF_FILE_NAME;
	private static Properties properties;
	
	private static String APP_KEY = null;
	private static String MASTER_SECRET = null;
	
	public static long g_alive_time = ALIVE_TIME; //过期时间默认3天
	public static boolean g_apns = APNS; //默认不推送生产环境
	
	public JPushClient jpushClient;
	
	@Bean
    public JPushClient jpushClient(){
		logger.info("jpushClient 注入:" + (jpushClient != null));
		return jpushClient;
    }
	
	public JPushConfig(){
		init();
	}
	
	public JPushConfig(String confFile){
		if(!StringUtils.isEmpty(confFile)){
			g_conf_file = confFile;
		}
		init();
	}

	public void init() {
		LiuziUtil.tag("  --------  Liuzi JPush初始化......  --------");
		
		logger.info("======== jPush初始化，加载配置" + g_conf_file + " ========");
		
		if(properties == null){
			try (InputStream in = PropertyUtils.class.getClassLoader().getResourceAsStream(g_conf_file)){
				properties = new Properties();
				properties.load(in);
			} catch (IOException e) {
				logger.error("jPush初始化失败，错误：" + e.getMessage());
				e.printStackTrace();
				return;
			}
		}
		
		try {
			APP_KEY = properties.getProperty("jPush.app_key");
			MASTER_SECRET = properties.getProperty("jPush.master_secret");
			jpushClient = new JPushClient(MASTER_SECRET, APP_KEY);
			
			Object alt = properties.getProperty("jPush.alive_time");
			if(!StringUtils.isEmpty(alt)){
				g_alive_time = Long.parseLong(alt.toString());
			}
			Object apns = properties.getProperty("jPush.apns");
			if(!StringUtils.isEmpty(apns)){
				g_apns = Boolean.parseBoolean(apns.toString());
			}
		} catch (NumberFormatException e) {
			logger.error("jPush初始化失败，错误：" + e.getMessage());
			e.printStackTrace();
			return;
		}
		
		logger.info("======== jPush初始化完成 ========\n");
	}
}
