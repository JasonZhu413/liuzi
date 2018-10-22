package com.liuzi.rest.boot;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.liuzi.util.LiuziUtil;

@Slf4j
@Configuration
public class RestConfig {
	
	@Value("${rest.connectTimeOut}")
	private static String g_connectTimeOut = "10000";
	@Value("${rest.readTimeOut}")
	private static String g_readTimeOut = "10000";
	
	@Bean
    public RestTemplate restTemplate(){
		LiuziUtil.tag("  --------  Liuzi Rest初始化......  --------");
		
		SimpleClientHttpRequestFactory httpClientFactory = new SimpleClientHttpRequestFactory();
		httpClientFactory.setConnectTimeout(Integer.parseInt(g_connectTimeOut));
		httpClientFactory.setReadTimeout(Integer.parseInt(g_readTimeOut));
		
		RestTemplate restTemplate = new RestTemplate(httpClientFactory);
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
		messageConverters.add(new FormHttpMessageConverter());
		messageConverters.add(new MappingJackson2HttpMessageConverter());
		messageConverters.add(new MappingJackson2XmlHttpMessageConverter());
		StringHttpMessageConverter shmc = new StringHttpMessageConverter(Charset.forName("UTF-8"));
		messageConverters.add(shmc);
		restTemplate.setMessageConverters(messageConverters);
		
		log.info("  --------  Liuzi Rest初始化完成......  --------");
		
		return restTemplate;
    }
}
