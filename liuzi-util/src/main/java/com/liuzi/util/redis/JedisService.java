package com.liuzi.util.redis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

@Service("jedisService")
public class JedisService {
	
	private static final int LOCK_TIME = 2000;
	private static final String LOCK_SUCCESS = "OK";
	private static final Long RELEASE_SUCCESS = 1L;
	private static final String IF_NOT_EXIST = "NX";
    private static final String WITH_EXPIRE_TIME = "PX";
    private static final String script = "if redis.call('get', KEYS[1]) == ARGV[1] " + 
    		"then return redis.call('del', KEYS[1]) else return 0 end";
    
    /*@Autowired
	private RedisFactory redisFactory;*/
    
    @Autowired
    private JedisPool jedisPool;
    
    private Jedis resource(){
		return jedisPool.getResource();
	}
    
	public void set(String key, String value){
	    try (Jedis jedis = resource()){
	    	jedis.set(key, value); 
	    } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public void set(String key, String value, int time){
	    try (Jedis jedis = resource()){
	    	jedis.setex(key, time, value);
	    }catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public void set(String key, Object value){
	    try (Jedis jedis = resource()){
	    	jedis.set(key, JSONObject.fromObject(value).toString()); 
	    } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public void set(String key, Object value, int time){
	    try (Jedis jedis = resource()){
	    	jedis.setex(key, time, JSONObject.fromObject(value).toString());
	    }catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public <T> void hset(String key, String field, T t){
 	    try (Jedis jedis = resource()){ 
 	    	jedis.hset(key, field, JSONObject.fromObject(t).toString());
 	    }catch (Exception e){
             e.printStackTrace();
        }
	}
	
	public <T> void hset(String key, String field, T t, int time){
 	    try (Jedis jedis = resource()){
 	    	Transaction tran = jedis.multi();
 	    	tran.hset(key, field, JSONObject.fromObject(t).toString());
 	    	tran.expire(key, time);
 	    	tran.exec();
 	    }catch (Exception e) {  
             e.printStackTrace();  
        }
	}
	
	public <T> void lset(String key, List<T> list){
		try (Jedis jedis = resource()){
			Transaction tran = jedis.multi();
			for(T t : list){
				tran.lpush(key, JSONObject.fromObject(t).toString());
			}
			tran.exec();
 	    }catch (Exception e) {  
            e.printStackTrace();  
        } 
	}
	
	public <T> void lset(String key ,int time, List<T> list){
 	    try (Jedis jedis = resource()){
 	    	Transaction tran = jedis.multi();
 	    	for(T t : list){
 	    		tran.lpush(key, JSONObject.fromObject(t).toString());
			}
 	    	tran.expire(key.getBytes(), time);
 	    	tran.exec();
 	    }catch (Exception e) {  
             e.printStackTrace();  
        }
	}
	
	public <T> void rset(String key, List<T> list){
		try (Jedis jedis = resource()){
			Transaction tran = jedis.multi();
			for(T t : list){
				jedis.rpush(key, JSONObject.fromObject(t).toString());
			}
 	    	tran.exec();
 	    }catch (Exception e) {  
            e.printStackTrace();  
        } 
	}
	
	public <T> void rset(String key ,int time, List<T> list){
 	    try (Jedis jedis = resource()){
 	    	Transaction tran = jedis.multi();
 	    	for(T t : list){
 	    		tran.rpush(key, JSONObject.fromObject(t).toString());
			}
 	    	tran.expire(key.getBytes(), time);
 	    	tran.exec();
 	    }catch (Exception e) {  
             e.printStackTrace();  
        }
	}
	
	public Object get(String key){
	    try (Jedis jedis = resource()){
	    	return jedis.get(key);
	    }catch (Exception e) {  
            e.printStackTrace();
        }
	    return null;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T hget(String key, String filed){
	    try (Jedis jedis = resource()){
	    	return (T) jedis.hget(key, filed);
	    }catch (Exception e) {
            e.printStackTrace();
        }
	    return null;
	}
	
	@SuppressWarnings("unchecked")
	public <T> Map<String, T> hgetAll(String key, String filed){
	    try (Jedis jedis = resource()){
	    	Map<String, T> rMap = new HashMap<>();
	    	Map<String, String> map = jedis.hgetAll(key);
	    	for(Map.Entry<String, String> entry : map.entrySet()){
	    		JSONObject json = JSONObject.fromObject(entry.getValue());
	    		rMap.put(entry.getKey(), (T) json);
	    	}
	    	return rMap;
	    }catch (Exception e) {
            e.printStackTrace();
        }
	    return null;
	}
	
	public Long llen(String key){
		Long count = 0L;
 	    try(Jedis jedis = resource()){ 
 	    	count = jedis.llen(key.getBytes());
 	    }catch (Exception e) {  
             e.printStackTrace();  
        }
 	    return count;
	}
	
	public <T> List<T> getList(String key){
		return lrange(key, 0, -1);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T lget(String key){
	    try (Jedis jedis = resource()){
	    	return (T)jedis.lpop(key);
	    }catch (Exception e) {  
            e.printStackTrace();  
        }
	    return null;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T rget(String key){
	    try (Jedis jedis = resource()){
	    	return (T)jedis.rpop(key);
	    }catch (Exception e) {  
            e.printStackTrace();  
        }
	    return null;
	}
	
	public Long length(String key){
	    try (Jedis jedis = resource()){
	    	return jedis.llen(key);
	    }catch (Exception e) {  
            e.printStackTrace();
        }
	    return 0L;
	}
	
	public String ltrim(String key){
	    return ltrim(key, 0, 0);
	}
	
	public String ltrim(String key, int start, int stop){
	    try (Jedis jedis = resource()){
	    	return jedis.ltrim(key, start, stop);
	    }catch (Exception e) {
            e.printStackTrace();
        }
	    return null;
	}
	
	public void del(String key){
 	    try (Jedis jedis = resource()){
 	    	jedis.del(key);
 	    }catch (Exception e) {  
             e.printStackTrace();  
 	    }
	}
	
	public Long incr(String key){
		try (Jedis jedis = resource()){ 
 	    	return jedis.incr(key);
 	    }catch (Exception e) {
             e.printStackTrace();  
        }
		return 0L;
	}
	
	public Long incr(String key, int by){
 	    try (Jedis jedis = resource()){ 
 	    	return jedis.incrBy(key, by);
 	    }catch (Exception e) {
             e.printStackTrace();
        }
 	    return 0L;
	}
	
	public Long zcard(String key){
 	    try (Jedis jedis = resource()){ 
 	    	return jedis.zcard(key);
 	    }catch (Exception e) {
             e.printStackTrace();  
        }
 	    return 0L;
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> lrange(String key, long start, long end){
		List<T> list = new ArrayList<>();
 	    try(Jedis jedis = resource()){ 
 	    	List<String> l = jedis.lrange(key, start, end);
 	    	l.forEach(s -> list.add((T) s));
 	    }catch (Exception e) {  
             e.printStackTrace();  
        }
 	    return list;
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> zrange(String key, long start, long end){
		List<T> list = new ArrayList<>();
 	    try (Jedis jedis = resource()){ 
 	    	Set<String> set = jedis.zrange(key, start, end);
 	    	set.forEach(s -> list.add((T) s));
 	    }catch (Exception e) {
             e.printStackTrace();  
        }
 	    return list;
	}
   
	@SuppressWarnings("unchecked")
	public <T> List<T> zrangeByScore(String key, double start, double end){
		List<T> list = new ArrayList<>();
 	    try (Jedis jedis = resource()){ 
 	    	Set<String> set = jedis.zrangeByScore(key, start, end);
 	    	set.forEach(s -> list.add((T)s));
 	    }catch (Exception e) {
             e.printStackTrace();  
        }
 	    return list;
	}
	
	
	
	public Long zremrangeByScore(String key, double start, double end){
 	    try (Jedis jedis = resource()){
 	    	return jedis.zremrangeByScore(key, start, end);
 	    }catch (Exception e) {
             e.printStackTrace();  
        }
 	    return 0L;
	}
	
	public void zset(String key, Object obj, double score){
 	    try (Jedis jedis = resource()){ 
 	    	jedis.zadd(key, score, JSONObject.fromObject(obj).toString());
 	    }catch (Exception e){
             e.printStackTrace();  
        }
	}
	
	public Long zcount(String key, double start, double end){
 	    try (Jedis jedis = resource()){ 
 	    	Long count = jedis.zcount(key, start, end);
 	    	return count == null ? 0 : count;
 	    }catch (Exception e){
             e.printStackTrace();  
        }
 	    return 0L;
	}
	
	/**
	 * 锁
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean lock(String key, String value){
		return lock(key, value, LOCK_TIME);
	}
	
	/**
	 * 锁
	 * @param key
	 * @param value
	 * @param time 锁持续时长（毫秒）
	 * @return
	 */
	public boolean lock(String key, String value, int time){
		try (Jedis jedis = resource()){ 
			String ok = jedis.set(key, value, IF_NOT_EXIST, WITH_EXPIRE_TIME, time);
	    	if (LOCK_SUCCESS.equals(ok)) {
	    		return true;
	    	}
 	    }catch (Exception e){
 	    	e.printStackTrace();
 	    } 
 	   	return false;
	}
	
	/**
	 * 释放锁，匹配value
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean release(String key, String value) {
		try (Jedis jedis = resource()){ 
 	        Object result = jedis.eval(script, Collections.singletonList(key), 
 	        		Collections.singletonList(value));
 	        if (RELEASE_SUCCESS.equals(result)) {
 	            return true;
 	        }
 	    }catch (Exception e){
 	    	e.printStackTrace();
 	    } 
		return false;
    }
	
	/**
	 * 释放锁，不匹配value
	 * @param key
	 * @return
	 */
	public void release(String key) {
		del(key);
    }
	
	
	/**
	 * 清空
	 */
	public String flushAll(String key) {
		try (Jedis jedis = resource()){ 
			return jedis.flushAll();
 	    }catch (Exception e){
 	    	e.printStackTrace();
 	    } 
		return null;
    }
	
	
	public void main(String []args){
		
		
	}
}
