package com.liuzi.util.redis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;



public class RedisPool {
	
	private static final int LOCK_TIME = 1000;
	private static final String LOCK_SUCCESS = "OK";
	private static final Long RELEASE_SUCCESS = 1L;
	private static final String IF_NOT_EXIST = "NX";
    private static final String WITH_EXPIRE_TIME = "PX";
    private static final String SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] " + 
    		"then return redis.call('del', KEYS[1]) else return 0 end";
    
    private JedisPool jedisPool;
    
    public RedisPool(JedisPoolConfig jedisPoolConfig, String host, int port,
    		int timeout, String password){
    	jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password);
    }
    
    /**
     * 获取资源
     */
    private Jedis get(){
		return jedisPool.getResource();
	}
    /**
     * 释放资源
     */
    private void close(Jedis jedis){
    	if(jedis != null){
    		jedis.close();
    	}
	}
    
    public boolean exists(String key){
    	Jedis jedis = get();
    	try {
	    	return jedis.exists(key); 
	    } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	close(jedis);
        }
    	return false;
    }
    
	public void set(String key, String value){
		Jedis jedis = get();
	    try {
	    	jedis.set(key, value); 
	    } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	close(jedis);
        }
	}
	
	public void set(String key, String value, int time){
		Jedis jedis = get();
	    try {
	    	jedis.setex(key, time, value);
	    }catch (Exception e) {
            e.printStackTrace();
        } finally {
        	close(jedis);
        }
	}
	
	public <T> void hset(String key, String field, T t){
		Jedis jedis = get();
	    try {
 	    	jedis.hset(key, field, JSONObject.fromObject(t).toString());
 	    } catch (Exception e) {
             e.printStackTrace();
        } finally {
        	close(jedis);
        }
	}
	
	public <T> void hset(String key, String field, T t, int time){
		Jedis jedis = get();
	    try {
	    	jedis.hset(key, field, JSONObject.fromObject(t).toString());
	    	jedis.expire(key, time);
 	    }catch (Exception e) {
             e.printStackTrace();  
        } finally {
        	close(jedis);
        }
	}
	
	public <T> void lpush(String key, List<T> list){
		Jedis jedis = get();
	    try {
			for(T t : list){
				jedis.lpush(key, JSONObject.fromObject(t).toString());
			}
 	    }catch (Exception e) {  
            e.printStackTrace();  
        } finally {
        	close(jedis);
        }
	}
	
	public <T> void lpush(String key, int time, List<T> list){
		Jedis jedis = get();
	    try {
 	    	for(T t : list){
 	    		jedis.lpush(key, JSONObject.fromObject(t).toString());
			}
 	    	jedis.expire(key, time);
 	    }catch (Exception e) {  
             e.printStackTrace();  
        } finally {
        	close(jedis);
        }
	}
	
	public <T> void rpush(String key, List<T> list){
		Jedis jedis = get();
	    try {
			for(T t : list){
				jedis.rpush(key, JSONObject.fromObject(t).toString());
			}
 	    }catch (Exception e) {  
            e.printStackTrace();  
        } finally {
        	close(jedis);
        }
	}
	
	public <T> void rpush(String key ,int time, List<T> list){
		Jedis jedis = get();
	    try {
 	    	for(T t : list){
 	    		jedis.rpush(key, JSONObject.fromObject(t).toString());
			}
 	    	jedis.expire(key, time);
 	    }catch (Exception e) {  
             e.printStackTrace();  
        } finally {
        	close(jedis);
        }
	}
	
	public String get(String key){
		Jedis jedis = get();
	    try {
	    	return jedis.get(key);
	    }catch (Exception e) {  
            e.printStackTrace();
        } finally {
        	close(jedis);
        }
	    return null;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T hget(String key, String filed){
		Jedis jedis = get();
	    try {
	    	return (T) jedis.hget(key, filed);
	    }catch (Exception e) {
            e.printStackTrace();
        } finally {
        	close(jedis);
        }
	    return null;
	}
	
	@SuppressWarnings("unchecked")
	public <T> Map<String, T> hgetAll(String key){
		Jedis jedis = get();
	    try {
	    	Map<String, T> rMap = new HashMap<>();
	    	Map<String, String> map = jedis.hgetAll(key);
	    	for(Map.Entry<String, String> entry : map.entrySet()){
	    		JSONObject json = JSONObject.fromObject(entry.getValue());
	    		rMap.put(entry.getKey(), (T) json);
	    	}
	    	return rMap;
	    }catch (Exception e) {
            e.printStackTrace();
        } finally {
        	close(jedis);
        }
	    return null;
	}
	
	public Long llen(String key){
		Jedis jedis = get();
	    try { 
	    	return jedis.llen(key);
 	    } catch (Exception e) {  
             e.printStackTrace();  
        } finally {
        	close(jedis);
        }
 	    return 0L;
	}
	
	public <T> List<T> lrange(String key){
		return lrange(key, 0, -1);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T lpop(String key){
		Jedis jedis = get();
	    try {
	    	return (T)jedis.lpop(key);
	    }catch (Exception e) {  
            e.printStackTrace();  
        } finally {
        	close(jedis);
        }
	    return null;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T rpop(String key){
		Jedis jedis = get();
	    try {
	    	return (T)jedis.rpop(key);
	    }catch (Exception e) {  
            e.printStackTrace();  
        } finally {
        	close(jedis);
        }
	    return null;
	}
	
	public String ltrim(String key){
	    return ltrim(key, 0, 0);
	}
	
	public String ltrim(String key, int start, int stop){
		Jedis jedis = get();
	    try {
	    	return jedis.ltrim(key, start, stop);
	    }catch (Exception e) {
            e.printStackTrace();
        } finally {
        	close(jedis);
        }
	    return null;
	}
	
	public void del(String key){
		Jedis jedis = get();
	    try {
 	    	jedis.del(key);
 	    }catch (Exception e) {  
            e.printStackTrace();  
 	    } finally {
        	close(jedis);
        }
	}
	
	public Long incr(String key){
		Jedis jedis = get();
	    try { 
 	    	return jedis.incr(key);
 	    }catch (Exception e) {
            e.printStackTrace();  
        } finally {
        	close(jedis);
        }
		return 0L;
	}
	
	public Long incr(String key, int by){
		Jedis jedis = get();
	    try { 
 	    	return jedis.incrBy(key, by);
 	    }catch (Exception e) {
            e.printStackTrace();
        }
 	    return 0L;
	}
	
	public Long zcard(String key){
		Jedis jedis = get();
	    try { 
 	    	return jedis.zcard(key);
 	    }catch (Exception e) {
            e.printStackTrace();  
        } finally {
        	close(jedis);
        }
 	    return 0L;
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> lrange(String key, long start, long end){
		List<T> list = new ArrayList<>();
		Jedis jedis = get();
	    try { 
 	    	List<String> l = jedis.lrange(key, start, end);
 	    	l.forEach(s -> list.add((T) s));
 	    } catch (Exception e) {
             e.printStackTrace();  
        } finally {
        	close(jedis);
        }
 	    return list;
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> zrange(String key, long start, long end){
		List<T> list = new ArrayList<>();
		Jedis jedis = get();
	    try { 
 	    	Set<String> set = jedis.zrange(key, start, end);
 	    	set.forEach(s -> list.add((T) s));
 	    } catch (Exception e) {
             e.printStackTrace();  
        } finally {
        	close(jedis);
        }
 	    return list;
	}
   
	@SuppressWarnings("unchecked")
	public <T> List<T> zrangeByScore(String key, double start, double end){
		List<T> list = new ArrayList<>();
		Jedis jedis = get();
	    try { 
 	    	Set<String> set = jedis.zrangeByScore(key, start, end);
 	    	set.forEach(s -> list.add((T)s));
 	    }catch (Exception e) {
             e.printStackTrace();  
        } finally {
        	close(jedis);
        }
 	    return list;
	}
	
	public Long zremrangeByScore(String key, double start, double end){
		Jedis jedis = get();
	    try {
 	    	return jedis.zremrangeByScore(key, start, end);
 	    }catch (Exception e) {
             e.printStackTrace();  
        } finally {
        	close(jedis);
        }
 	    return 0L;
	}
	
	public void zadd(String key, String value, double score){
		Jedis jedis = get();
	    try { 
 	    	jedis.zadd(key, score, value);
 	    } catch (Exception e) {
             e.printStackTrace();  
        } finally {
        	close(jedis);
        }
	}
	
	public Long zcount(String key, double start, double end){
		Jedis jedis = get();
	    try { 
 	    	Long count = jedis.zcount(key, start, end);
 	    	return count == null ? 0 : count;
 	    } catch (Exception e) {
             e.printStackTrace();  
        } finally {
        	close(jedis);
        }
 	    return 0L;
	}
	
	public void setBatch(Map<String, String> kv){
		Jedis jedis = get();
	    try {
	    	Pipeline p = jedis.pipelined();
	    	for(Entry<String, String> entry : kv.entrySet()){
	    		p.set(entry.getKey(), entry.getValue());
	    	}
	    	p.sync();
 	    } catch (Exception e) {
            e.printStackTrace();  
        } finally {
        	close(jedis);
        }
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
		Jedis jedis = get();
	    try { 
			String ok = jedis.set(key, value, IF_NOT_EXIST, WITH_EXPIRE_TIME, time);
	    	if (LOCK_SUCCESS.equals(ok)) {
	    		return true;
	    	}
 	    } catch (Exception e) {
 	    	e.printStackTrace();
 	    } finally {
        	close(jedis);
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
		Jedis jedis = get();
	    try { 
 	        Object result = jedis.eval(SCRIPT, Collections.singletonList(key), 
 	        		Collections.singletonList(value));
 	        if (RELEASE_SUCCESS.equals(result)) {
 	            return true;
 	        }
 	    } catch (Exception e) {
 	    	e.printStackTrace();
 	    } finally {
        	close(jedis);
        }
		return false;
    }
	
	
	
	public void main(String []args){
		
		
	}
}
