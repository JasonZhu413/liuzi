package com.liuzi.rest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.liuzi.util.LiuziUtil;

@Configuration
public class RestConfig {
	private static Logger logger = LoggerFactory.getLogger(RestConfig.class);
	
	private final static String DEFAULT_CONF_FILE_NAME = "conf/rest.properties";
	private final static String DEFAULT_CONNECT_TIME_OUT = "10000";
	private final static String DEFAULT_READ_TIME_OUT = "10000";
	
	private static Properties properties;
	private static String g_conf_file = DEFAULT_CONF_FILE_NAME;
	
	private static String g_connectTimeOut = DEFAULT_CONNECT_TIME_OUT;
	private static String g_readTimeOut = DEFAULT_READ_TIME_OUT;
	
	public RestTemplate restTemplate;
	
	@Bean
    public RestTemplate restTemplate(){
		logger.info("restTemplate 注入:" + (restTemplate != null));
		return restTemplate;
    }
	
	public RestConfig(){
		init();
	}
	
	public RestConfig(String fileName){
		if(!StringUtils.isEmpty(fileName)) g_conf_file = fileName;
        init();
	}

	public void init() {
		LiuziUtil.tag("  --------  Liuzi Rest初始化......  --------");
		
		logger.info("======== rest初始化，加载配置" + g_conf_file + " ========");
		if(properties == null){
			try (InputStream in = PropertyUtils.class.getClassLoader().getResourceAsStream(g_conf_file)){
				properties = new Properties();
				properties.load(in);
			} catch (IOException e) {
				logger.error("rest初始化失败，错误：" + e.getMessage());
				e.printStackTrace();
				return;
			}
		}
		
		try {
			String connectTimeOut = properties.getProperty("rest.connectTimeOut");
			String readTimeOut = properties.getProperty("rest.readTimeOut");
			
			if(!StringUtils.isEmpty(connectTimeOut)){
				g_connectTimeOut = connectTimeOut;
			}
			if(!StringUtils.isEmpty(readTimeOut)){
				g_readTimeOut = readTimeOut;
			}
			
			SimpleClientHttpRequestFactory httpClientFactory = new SimpleClientHttpRequestFactory();
			httpClientFactory.setConnectTimeout(Integer.parseInt(g_connectTimeOut));
			httpClientFactory.setReadTimeout(Integer.parseInt(g_readTimeOut));
			
			restTemplate = new RestTemplate(httpClientFactory);
			
			/*List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
			messageConverters.add(new FormHttpMessageConverter());
			messageConverters.add(new MappingJackson2HttpMessageConverter());
			//messageConverters.add(new MappingJackson2XmlHttpMessageConverter());
			StringHttpMessageConverter shmc = new StringHttpMessageConverter(Charset.forName("UTF-8"));
			messageConverters.add(shmc);
			restTemplate.setMessageConverters(messageConverters);*/
			
		} catch (NumberFormatException e) {
			logger.error("rest初始化失败，错误：" + e.getMessage());
			e.printStackTrace();
			return;
		}
		logger.info("======== rest初始化完成 ========\n");
	}
}
