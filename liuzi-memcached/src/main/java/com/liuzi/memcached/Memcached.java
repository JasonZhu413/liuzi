package com.liuzi.memcached;

import java.io.PrintWriter;  
import java.io.StringWriter;  
import java.util.Date;  
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;

import com.liuzi.util.common.Log;
import com.liuzi.util.encrypt.AESUtil;
import com.whalin.MemCached.MemCachedClient;
  
/** 
 * Memcached工具类 
 * @author zsy 
 */  
public class Memcached{
	
	@Autowired
	private MemCachedClient memCachedClient;
	
    /** 
     * 向缓存添加新的键值对。如果键已经存在，则之前的值将被替换。 
     * @param key 键
     * @param value 值
     * @return 
     */  
    public boolean set(String key, Object value) {  
        return setExp(key, value, null);  
    }  
    
    /** 
     * 向缓存添加新的键值对。如果键已经存在，则之前的值将被替换。 
     * @param key 键 
     * @param value 值 
     * @param expire 过期时间(秒)
     * @return 
     */  
    public boolean set(String key, Object value, long time) {  
        return setExp(key, value, getExpire(time, TimeUnit.SECONDS));  
    }
    
    /** 
     * 向缓存添加新的键值对。如果键已经存在，则之前的值将被替换。 
     * @param key 键 
     * @param value 值 
     * @param expire 过期时间
     * @param timeUnit 过期时间单位
     * @return 
     */  
    public boolean set(String key, Object value, long time, TimeUnit timeUnit) {  
        return setExp(key, value, getExpire(time, timeUnit));
    }
  
    private boolean setExp(String key, Object value, Date expire) {  
        boolean flag = false;  
        try {
            flag = memCachedClient.set(key, value, expire);  
        } catch (Exception e) {  
            Log.error(e, "Memcached set错误，key：{}", key);  
        }  
        return flag;  
    }  
  
    /** 
     * 仅当缓存中不存在键时，add 命令才会向缓存中添加一个键值对。 
     * @param key 键 
     * @param value 值 
     * @return 
     */  
    public boolean add(String key, Object value) {  
        return addExp(key, value, null);
    }  
  
    /** 
     * 仅当缓存中不存在键时，add 命令才会向缓存中添加一个键值对。 
     * @param key 键 
     * @param value 值 
     * @param time 过期时间(秒)
     * @return 
     */  
    public boolean add(String key, Object value, long time) {
        return addExp(key, value, getExpire(time, TimeUnit.SECONDS));  
    }  
    
    /** 
     * 仅当缓存中不存在键时，add 命令才会向缓存中添加一个键值对。 
     * @param key 键 
     * @param value 值 
     * @param time 过期时间
     * @param timeUnit 过期时间单位
     * @return 
     */  
    public boolean add(String key, Object value, long time, TimeUnit timeUnit) {
        return addExp(key, value, getExpire(time, timeUnit));
    }  
    
    private boolean addExp(String key, Object value, Date expire) {  
        boolean flag = false;
        try {  
            flag = memCachedClient.add(key, value, expire);  
        } catch (Exception e) {  
        	Log.error(e, "Memcached add错误，key：{}", key);  
        }  
        return flag;  
    }  
  
    /** 
     * 仅当键已经存在时，replace 命令才会替换缓存中的键。 
     * @param key 键 
     * @param value 值 
     * @return 
     */  
    public boolean replace(String key, Object value) {  
        return replaceExp(key, value, null);  
    }  
  
    /** 
     * 仅当键已经存在时，replace 命令才会替换缓存中的键。 
     * @param key 键 
     * @param value 值 
     * @param time 过期时间(秒)
     * @return 
     */  
    public boolean replace(String key, Object value, long time) {  
        return replaceExp(key, value, getExpire(time, TimeUnit.SECONDS));  
    }  
    
    /** 
     * 仅当键已经存在时，replace 命令才会替换缓存中的键。 
     * @param key 键 
     * @param value 值 
     * @param time 过期时间
     * @param timeUnit 过期时间单位
     * @return 
     */  
    public boolean replace(String key, Object value, long time, TimeUnit timeUnit) {  
        return replaceExp(key, value, getExpire(time, timeUnit));  
    }  
  
    private boolean replaceExp(String key, Object value, Date expire) {  
        boolean flag = false;  
        try {  
            flag = memCachedClient.replace(key, value, expire);  
        } catch (Exception e) {  
        	Log.error(e, "Memcached replace错误，key：{}", key);  
        }  
        return flag;  
    }  
  
    /** 
     * get 命令用于检索与之前添加的键值对相关的值。 
     * @param key 键 
     * @return 
     */  
    public Object get(String key) {  
        Object obj = null;  
        try {  
            obj = memCachedClient.get(key);  
        } catch (Exception e) {  
        	Log.error(e, "Memcached get错误，key：{}", key);  
        }  
        return obj;  
    }  
  
    /** 
     * 删除 memcached 中的任何现有值。 
     * @param key 键 
     * @return 
     */  
    public boolean delete(String key) {  
        boolean flag = false;  
        try {  
            flag = memCachedClient.delete(key);  
        } catch (Exception e) {  
        	Log.error(e, "Memcached delete错误，key：{}", key);  
        }  
        return flag;  
    }  
    
    /** 
     * 递增1 
     * @param key 键 
     * @return 
     */  
    public long incr(String key) {  
        return incr(key, 1);  
    }  
    
    /** 
     * 递增 
     * @param key 键 
     * @param inc 递增因子 
     * @return 
     */  
    public long incr(String key, long inc) {  
        long flag = 0;  
        try {  
        	if(inc <= 0){
        		Log.warn("Memcached incr递增因子必须大于0");
        		throw new IllegalArgumentException("递增因子必须大于0");
        	}
            flag = memCachedClient.incr(key, inc);
        } catch (Exception e) {  
        	Log.error(e, "Memcached incr错误，key：{}", key);  
        }  
        return flag;  
    } 
    
    /** 
     * 递减1 
     * @param key 键 
     * @return 
     */  
    public long decr(String key) {  
        return decr(key, 1);
    }  
    
    /** 
     * 递减
     * @param key 键 
     * @param inc 递减因子 
     * @return 
     */  
    public long decr(String key, long inc) {  
        long flag = 0;  
        try {  
        	if(inc <= 0){
        		Log.warn("Memcached incr递减因子必须大于0");
        		throw new IllegalArgumentException("递减因子必须大于0");
        	}
            flag = memCachedClient.decr(key, inc);
        } catch (Exception e) {
        	Log.error(e, "Memcached decr错误，key：{}", key);
        }  
        return flag;  
    } 
    
    /** 
     * 清理缓存中的所有键/值对 
     *  
     * @return 
     */  
    /*public boolean flashAll() {  
        boolean flag = false;  
        try {  
            flag = memCachedClient.flushAll();  
        } catch (Exception e) {  
        	log.info("Memcached flashAll方法报错\r\n" + exceptionWrite(e));  
        }  
        return flag;  
    }  */
  
    /** 
     * 返回异常栈信息，String类型 
     *  
     * @param e 
     * @return 
     */  
    private String exceptionWrite(Exception e) {  
        StringWriter sw = new StringWriter();  
        PrintWriter pw = new PrintWriter(sw);  
        e.printStackTrace(pw);  
        pw.flush();  
        return sw.toString();  
    }  
    
    private static Date getExpire(long time, TimeUnit timeUnit){
    	time = time <= 0 ? 0 : time;
    	if(timeUnit == null || time == 0){
    		return new Date(time * 1000);
    	}
    	
    	long endTime = 0;
    	if(timeUnit.compareTo(TimeUnit.DAYS) == 0){
    		//日
        	endTime = time * 24 * 60 * 60 * 1000;
    	} else if (timeUnit.compareTo(TimeUnit.HOURS) == 0) {
    		//时
        	endTime = time * 60 * 60 * 1000;
        } else if (timeUnit.compareTo(TimeUnit.MINUTES) == 0) {
        	//分
        	endTime = time * 60 * 1000;
        } else if (timeUnit.compareTo(TimeUnit.MILLISECONDS) == 0) {
        	//毫秒
    		endTime = time;
        } else {
        	//秒
    		endTime = time * 1000;
        }
    	return new Date(endTime);
    }
    
    public static void main(String[] args) {
    	//mONOir5ZsnfhmxNB91Zx9A==
    	System.out.println(AESUtil.encrypt("15210050811"));
	}
}  
