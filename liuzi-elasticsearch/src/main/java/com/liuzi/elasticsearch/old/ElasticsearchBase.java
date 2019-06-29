package com.liuzi.elasticsearch.old;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;




/**
 * index = database-${table}*或database-${table}-date(yyyyMMdd)
 * type = _doc
 * @author zsy
 */
public class ElasticsearchBase extends ElasticsearchTemplate{
	
	private static final String DEFAULT_TYPE = "_doc";
	private static final long DEFAULT_STIM = 3000;
	
	protected static String _index;
	protected static String _type = DEFAULT_TYPE;
	protected static long _scrollTimeInMillis = DEFAULT_STIM;
	
	private ElasticsearchBase(Client client) {
		super(client);
	}
	
	protected ElasticsearchBase(String server, String clusterName, String database, String type, 
			Long scrollTimeInMillis) {
		super(transportClient(server, clusterName, database, type, scrollTimeInMillis));
	}
	
	private static TransportClient transportClient(String server, String clusterName, String database, 
			String type, Long scrollTimeInMillis){
		if (StringUtils.isEmpty(clusterName)){
	    	throw new IllegalArgumentException("item \"clusterName\" not found");
	    }
		if (StringUtils.isEmpty(database)){
	    	throw new IllegalArgumentException("item \"database\" not found");
	    }
		
		String[] servers;
		int serversLength;
		if (StringUtils.isEmpty(server) || (serversLength = (servers = server.trim().split(",")).length) == 0){
	    	throw new IllegalArgumentException("item \"server\" not found");
	    }

	    TransportAddress[] address = new TransportAddress[serversLength];
	    try{
	    	String[] parts;
	    	for (int i = 0; i < serversLength; i++) {
	    		parts = servers[i].split("\\:", 2);
	  	      	if (parts.length != 2) {
	  	      		throw new IllegalArgumentException("the value of item \"server\" is invalid, the correct format is host:port");
	  	      	}
	  	      	int port = Integer.parseInt(parts[1]);
	  	      	address[i] = new TransportAddress(new InetSocketAddress(InetAddress.getByName(parts[0].trim()), port));
	  	    }
	    }catch(UnknownHostException e){
	    	throw new IllegalArgumentException("UnknownHostException: " + e.getMessage());
	    }
	    
	    _index = database;
	    if(!StringUtils.isBlank(type)){
	    	_type = type;
	    }
	    if(scrollTimeInMillis != null && scrollTimeInMillis >= 0){
	    	_scrollTimeInMillis = scrollTimeInMillis;
	    }
	    
	    Settings settings = Settings.builder()
                .put("cluster.name", clusterName)
                .put("client.transport.sniff", true)
                .build();
 
	    TransportClient transportClient = new PreBuiltTransportClient(settings);
	    transportClient.addTransportAddresses(address);
		//return new ElasticsearchTemplate(transportClient);
	    return transportClient;
	}
	
	/**
	 * 获取setting
	 * @param t
	 * @return
	 */
	protected <T> Document getDocument(T t) {
		Document annotation = t.getClass().getAnnotation(Document.class);
		if (annotation == null) {
			throw new IllegalArgumentException("Can't find annotation @Document on " + t.getClass().getName());
		}
		return annotation;
	}
	
	/**
	 * 获取字段名，若设置column则返回该值
	 * @param field
	 * @param column
	 * @return
	 */
	protected String getFieldName(Field field, String column) {
		return StringUtils.isNotBlank(column) ? column : field.getName();
	}
	
	/**
	 * 设置属性值
	 * @param field
	 * @param obj
	 * @param value
	 */
	protected void setFieldValue(Field field, Object obj, Object value) {
		boolean isAccessible = field.isAccessible();
		field.setAccessible(true);
		try {
			switch (field.getType().getSimpleName()) {
				case "BigDecimal":
					field.set(obj, new BigDecimal(value.toString()).setScale(5, BigDecimal.ROUND_HALF_UP));
					break;
				case "Long":
					field.set(obj, new Long(value.toString()));
					break;
				case "Integer":
					field.set(obj, new Integer(value.toString()));
					break;
				case "Date":
					field.set(obj, new Date(Long.valueOf(value.toString())));
					break;
				default:
					field.set(obj, value);
			}
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException(e);
		} finally {
			field.setAccessible(isAccessible);
		}
	}
	
	/**
	 * 获取字段值
	 * @param field
	 * @param obj
	 * @return
	 */
	protected Object getFieldValue(Field field, Object obj) {
		boolean isAccessible = field.isAccessible();
		field.setAccessible(true);
		try {
			return field.get(obj);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException(e);
		} finally {
			field.setAccessible(isAccessible);
		}
	}
	
	/**
	 * 转换为es识别的value值
	 * @param value
	 * @return
	 */
	protected Object formatValue(Object value) {
		if (value instanceof Date) {
			return ((Date) value).getTime();
		} else {
			return value;
		}
	}
	
	/**
	 * 获取索引分区数
	 * @param t
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected <T> int getShards(Class<T> clazz) {
		Map setting = this.getSetting(clazz);
		Object shards = setting.get(IndexMetaData.SETTING_NUMBER_OF_SHARDS);
		return shards == null ? 1 : Integer.parseInt(shards.toString());
	}
	
	/**
	 * 获取索引副本数
	 * @param t
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected <T> int getReplicas(Class<T> clazz) {
		Map setting = this.getSetting(clazz);
		Object replicas = setting.get(IndexMetaData.SETTING_NUMBER_OF_REPLICAS);
		return replicas == null ? 1 : Integer.parseInt(replicas.toString());
	}
}
