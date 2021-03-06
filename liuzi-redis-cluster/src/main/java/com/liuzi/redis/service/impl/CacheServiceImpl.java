package com.liuzi.redis.service.impl;

import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.liuzi.util.common.ConfigUtil;
import com.liuzi.redis.service.CacheService;
import com.liuzi.redis.service.RedisClusterService;


@Repository("cacheService")
public class CacheServiceImpl<T> implements CacheService {

	private static Logger logger = LoggerFactory.getLogger(CacheServiceImpl.class);
	
	@Resource
	private RedisClusterService redisClusterService;
	
	//User缓存时间
	public static final Integer SYS_USER_SEC = ConfigUtil.getInt("cache.time.user");
	
	
	public void save(String key,String value,Integer seconds){
		redisClusterService.save(key, value, seconds);
	}
	
	public Long saveArray(String key,List<Object> value,Integer seconds){
		return redisClusterService.saveArray(key, value, seconds);
	}
	
	public String get(String key){
		return redisClusterService.get(key);
	}
	
	public List<String> getArray(String key,Long start,Long end){
		return redisClusterService.getArray(key, start, end);
	}
	
	public void delete(String key){
		redisClusterService.delete(key);
	}
	
	@SuppressWarnings("unchecked")
	public T getEntry(Object key,Class<?> c) {
		if(StringUtils.isEmpty(key)){
			return null;
		}
		
		String realKey = "Sys_" + c.getName() + key;
		
		String str = redisClusterService.get(realKey.toString());
		
		T t;
		//redis查询到数据,转换数据类型 返回
		if(!StringUtils.isEmpty(str)){
			try {
				JSONObject json = JSONObject.fromObject(str);
				t = (T) JSONObject.toBean(json, c.newInstance().getClass());
				return t;
			} catch (Exception e) {
				e.printStackTrace();
				logger.debug("User[codeValue=?] toBean fail", new Object[]{key});
			}
		}
		
		/*redisDao.save(key, JSONObject.fromObject(t).toString(), SYS_USER_SEC);*/
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<T> getArray(String key) {
		if(StringUtils.isEmpty(key)){
			return null;
		}
		
		String realKey = "Sys_" + key;
		
		String str = redisClusterService.get(realKey.toString());
		
		List<T> list;
		//redis查询到数据,转换数据类型 返回
		if(!StringUtils.isEmpty(str)){
			try {
				JSONObject json = JSONObject.fromObject(str);
				list = (List<T>) JSONObject.toBean(json, List.class);
				return list;
			} catch (Exception e) {
				e.printStackTrace();
				logger.debug("User[codeValue=?] toBean fail", new Object[]{key});
			}
		}
		
		return null;
	}

}
