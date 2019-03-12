package com.liuzi.rest;

import java.nio.charset.Charset;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.SSLContext;

import lombok.Getter;
import lombok.Setter;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;


public class RestConfig {
	
	private static final Charset CHARSET = Charset.forName("UTF-8");
	
	/**
	 * 最大连接数, 默认值8
	 */
	@Getter @Setter private int maxTotal = 8;
	/**
	 * 最大并发数, 默认值5
	 */
	@Getter @Setter private int maxPerRoute = 5;
	/**
	 * 连接超时时间(毫秒), 默认值6000
	 */
	@Getter @Setter private int connectTimeout = 6000;
	/**
	 * 读写超时时间(毫秒), 默认值6000
	 */
	@Getter @Setter private int readTimeout = 6000;
	
	
    @Bean
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
    	PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
    	poolingHttpClientConnectionManager.setMaxTotal(maxTotal); // 连接池最大连接数  
    	poolingHttpClientConnectionManager.setDefaultMaxPerRoute(maxPerRoute); // 每个主机的并发
		return poolingHttpClientConnectionManager;
    }
    
    @Bean
    public CloseableHttpClient closeableHttpClient(PoolingHttpClientConnectionManager poolingHttpClientConnectionManager) {
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		httpClientBuilder.setConnectionManager(poolingHttpClientConnectionManager);
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		return closeableHttpClient;
    }
    
    @Bean
    public HttpComponentsClientHttpRequestFactory httpFactory(CloseableHttpClient closeableHttpClient) { 
		HttpComponentsClientHttpRequestFactory httpFactory = new HttpComponentsClientHttpRequestFactory();
		httpFactory.setHttpClient(closeableHttpClient);
		httpFactory.setConnectTimeout(connectTimeout);// 连接超时，毫秒		
		httpFactory.setReadTimeout(readTimeout); // 读写超时，毫秒		
		return httpFactory;
    }
    
    @Bean
    public RestTemplate restHttpTemplate(HttpComponentsClientHttpRequestFactory httpFactory) {
		RestTemplate restHttpTemplate = new RestTemplate(httpFactory);
        List<HttpMessageConverter<?>> messageConverters = restHttpTemplate.getMessageConverters();
        Iterator<HttpMessageConverter<?>> iterator = messageConverters.iterator();
        while (iterator.hasNext()) {
            HttpMessageConverter<?> converter = iterator.next();
            if (converter instanceof StringHttpMessageConverter) {
                iterator.remove();
            }
        }
        messageConverters.add(new StringHttpMessageConverter(CHARSET));
        return restHttpTemplate;
    }
    
    @Bean
    public CloseableHttpClient closeableHttpsClient(PoolingHttpClientConnectionManager poolingHttpClientConnectionManager) throws Exception{
    	TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
	    SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
	    SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
    	
    	HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		httpClientBuilder.setConnectionManager(poolingHttpClientConnectionManager);
		CloseableHttpClient closeableHttpsClient = httpClientBuilder.setSSLSocketFactory(csf).build();
    	
	    return closeableHttpsClient;
    }
    
    
    @Bean
    public HttpComponentsClientHttpRequestFactory httpsFactory(CloseableHttpClient closeableHttpsClient){
	    HttpComponentsClientHttpRequestFactory httpsFactory = new HttpComponentsClientHttpRequestFactory();
	    httpsFactory.setHttpClient(closeableHttpsClient);
	    httpsFactory.setConnectTimeout(connectTimeout);// 连接超时，毫秒		
	    httpsFactory.setReadTimeout(readTimeout); // 读写超时，毫秒	
	    return httpsFactory;
    }
    
    @Bean
    public RestTemplate restHttpsTemplate(HttpComponentsClientHttpRequestFactory httpsFactory) {
		RestTemplate restTemplate = new RestTemplate(httpsFactory);
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        Iterator<HttpMessageConverter<?>> iterator = messageConverters.iterator();
        while (iterator.hasNext()) {
            HttpMessageConverter<?> converter = iterator.next();
            if (converter instanceof StringHttpMessageConverter) {
                iterator.remove();
            }
        }
        messageConverters.add(new StringHttpMessageConverter(CHARSET));
        return restTemplate;
    }
}
