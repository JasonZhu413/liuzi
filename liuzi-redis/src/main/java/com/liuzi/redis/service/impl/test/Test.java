package com.liuzi.redis.service.impl.test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.cto.vo.ArticleTypeVo;
import com.liuzi.util.encrypt.MD5;

import redis.clients.jedis.JedisPoolConfig;

public class Test {

	public static void main(String[] args) {
		String host = "123.206.93.119";
		int port = 6380;
		int timeout = 10000;
		String password = "cto@_123";
		
		JedisPoolConfig jpc = new JedisPoolConfig();
		
		JedisConnectionFactory jcf = new JedisConnectionFactory();
		jcf.setHostName(host);
		jcf.setPort(port);
		jcf.setTimeout(timeout);
		jcf.setPassword(password);
		jcf.setUsePool(true);
		jcf.setUseSsl(false);
		jcf.setPoolConfig(jpc);
		jcf.afterPropertiesSet();

		RedisTemplate<String, Object> rt = new RedisTemplate<>();
		rt.setEnableTransactionSupport(false);
		rt.setConnectionFactory(jcf);
		rt.setKeySerializer(new StringRedisSerializer());
		rt.setValueSerializer(new JdkSerializationRedisSerializer());
		rt.setHashKeySerializer(new StringRedisSerializer());
		rt.setHashValueSerializer(new JdkSerializationRedisSerializer());
		rt.afterPropertiesSet();

		/*String key = "06b12055d6a1dd81ec7a56c76cdabb84";
		String str = redisPool.hget(key, 8 + "");
		print(JSONObject.toBean(JSONObject.fromObject(str), ArticleTypeVo.class));*/
		
		Map<String, Object> map = new LinkedHashMap<>();
		
		//
		String i = MD5.crypt("ARTICLE_TYPE_LIST_KEYS");
		String y = "ARTICLE_TYPE_LIST:";
		List<Object> keys = rt.opsForList().range(i, 0, -1);
		List<ArticleTypeVo> listVo = null;
		if(keys != null && keys.size() > 0){
			Map<Object, Object> hmap;
			for(Object obj: (List<Object>)keys.get(0)){
				hmap = rt.opsForHash().entries(MD5.crypt(y + obj));
				listVo = new ArrayList<>();
				for (Object value : hmap.values()) { 
					listVo.add((ArticleTypeVo) value);
					System.out.println(value);
				}
				//map.put(obj.toString(), listVo);
			}
		}
	}
	
	public static void print(Object obj){
		System.out.println(obj);
	}
}
