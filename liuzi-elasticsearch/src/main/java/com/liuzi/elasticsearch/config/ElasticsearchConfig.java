package com.liuzi.elasticsearch.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import lombok.Data;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestClientBuilder.HttpClientConfigCallback;
import org.elasticsearch.client.RestClientBuilder.RequestConfigCallback;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;

import com.liuzi.elasticsearch.repository.Elasticsearch;
import com.liuzi.elasticsearch.repository.ElasticsearchImpl;
import com.liuzi.elasticsearch.util.Constant;

@Data
public class ElasticsearchConfig{
	
	private int connectTimeOut = Constant.CONNECT_TIMEOUT;
	private int socketTimeOut = Constant.SOCKET_TIMEOUT;
	private int connectionRequestTimeOut = Constant.CONNECTION_REQUEST__TIMEOUT;
	private int maxConnectNum = Constant.MAX_CONNECTION_NUM;
	private int maxConnectPerRoute = Constant.MAX_CONNECTION_PER_ROUTE;
	
	private String server;
	private String database;
	private String type;
	private String spacer;
	private long searchAliveTime;
	
	@Bean
	public RestHighLevelClient client() {
		String[] servers;
		int serversLength;
		if (StringUtils.isEmpty(server) || (serversLength = (servers = server.trim().split(",")).length) == 0){
	    	throw new IllegalArgumentException("item \"server\" not found");
	    }

		HttpHost[] httpHosts = new HttpHost[serversLength];
	    try{
	    	String[] parts;
	    	HttpHost httpHost;
	    	for (int i = 0; i < serversLength; i++) {
	    		parts = servers[i].split("\\:", 2);
	  	      	if (parts.length != 2) {
	  	      		throw new IllegalArgumentException("the value of item \"server\" is invalid, the correct format is host:port");
	  	      	}
	  	      	int port = Integer.parseInt(parts[1]);
	  	      	
	  	      	httpHost = new HttpHost(InetAddress.getByName(parts[0].trim()), port);
	  	      	httpHosts[i] = new HttpHost(httpHost);
	  	    }
	    }catch(UnknownHostException e){
	    	throw new IllegalArgumentException("UnknownHostException: " + e.getMessage());
	    }
	    
	    Constant.init(database, type, spacer, searchAliveTime);
		
		RestClientBuilder builder = RestClient.builder(httpHosts);
		// 异步httpclient连接延时配置
		builder.setRequestConfigCallback(new RequestConfigCallback() {
			@Override
			public Builder customizeRequestConfig(Builder requestConfigBuilder) {
				requestConfigBuilder.setConnectTimeout(connectTimeOut);
				requestConfigBuilder.setSocketTimeout(socketTimeOut);
				requestConfigBuilder.setConnectionRequestTimeout(connectionRequestTimeOut);
				return requestConfigBuilder;
			}
		});
		// 异步httpclient连接数配置
		builder.setHttpClientConfigCallback(new HttpClientConfigCallback() {
			@Override
			public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
				httpClientBuilder.setMaxConnTotal(maxConnectNum);
				httpClientBuilder.setMaxConnPerRoute(maxConnectPerRoute);
				return httpClientBuilder;
			}
		});
		
		return new RestHighLevelClient(builder);
	}
	
	@Bean
	@DependsOn("client")
	public Elasticsearch elasticsearch() {
		return new ElasticsearchImpl();
	}
	
}
