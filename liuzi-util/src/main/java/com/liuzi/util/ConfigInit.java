package com.liuzi.util;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @Title:        ConfigUtil
 * 
 * @Description   获取application.properties属性内容
 * 
 * @author        ZhuShiyao
 * 
 * @Time          2016-11-23 11:02:29
 * 
 * @version       1.0
 * 
 */
public class ConfigInit {
	
	private static Logger logger = LoggerFactory.getLogger(ConfigInit.class);
	
	private static final String CONFIG_FILE = "conf/application.properties";
	
	protected static PropertiesConfiguration config;
	protected static String conf_file = CONFIG_FILE;
	
	public ConfigInit(){
		init();
	}
	
	public ConfigInit(String filePath){
		if(!StringUtils.isEmpty(filePath)){
			conf_file = filePath;
		}
		init();
	}
	
	public static void init(){
		LiuziUtil.tag(" --------  Liuzi Conf配置初始化...... --------");
		
		logger.info("===== application初始化，加载配置 " + conf_file + " ......========");
		
		config = new PropertiesConfiguration();
		config.setEncoding("UTF-8");
		
		try{
			config.load(conf_file);
			logger.info("初始化" + conf_file + "文件。。。。。。");
	    } catch (Exception e) {
	    	logger.error("初始化" + conf_file + "文件失败：" + e.getMessage());
	    	e.fillInStackTrace();
	    }
		
		logger.info("===== application初始化完成 ......========\n");
	}
	
}