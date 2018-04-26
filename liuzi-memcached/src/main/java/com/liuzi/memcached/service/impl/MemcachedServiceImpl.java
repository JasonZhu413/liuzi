package com.liuzi.memcached.service.impl;

import java.util.Date;  







import org.springframework.stereotype.Service;

import com.liuzi.memcached.Memcached;
import com.liuzi.memcached.service.MemcachedService;
import com.liuzi.util.MD5;
  
@Service()
public class MemcachedServiceImpl implements MemcachedService{  
  
    /** 
     * 向缓存添加新的键值对。如果键已经存在，则之前的值将被替换。 
     *  
     * @param key 
     *            键 
     * @param value 
     *            值 
     * @return 
     */  
	@Override
    public boolean set(String key, Object value) { 
		key = MD5.crypt(key);
        return Memcached.set(key, value, null);  
    }  
  
    /** 
     * 向缓存添加新的键值对。如果键已经存在，则之前的值将被替换。 
     *  
     * @param key 
     *            键 
     * @param value 
     *            值 
     * @param expire 
     *            过期时间 New Date(1000*10)：十秒后过期 
     * @return 
     */  
	@Override
    public boolean set(String key, Object value, Date expire) { 
		key = MD5.crypt(key);
        return Memcached.set(key, value, expire);  
    }  
	
	/** 
     * 向缓存添加新的键值对。如果键已经存在，则之前的值将被替换。 
     *  
     * @param key 
     *            键 
     * @param value 
     *            值 
     * @param time 
     *            过期时间 s
     * @return 
     */  
	@Override
    public boolean set(String key, Object value, int time) {  
		key = MD5.crypt(key);
        return Memcached.set(key, value, time);  
    }  
  
    /** 
     * 仅当缓存中不存在键时，add 命令才会向缓存中添加一个键值对。 
     *  
     * @param key 
     *            键 
     * @param value 
     *            值 
     * @return 
     */  
	@Override
    public boolean add(String key, Object value) {  
		key = MD5.crypt(key);
        return Memcached.add(key, value, null);  
    }  
  
    /** 
     * 仅当缓存中不存在键时，add 命令才会向缓存中添加一个键值对。 
     *  
     * @param key 
     *            键 
     * @param value 
     *            值 
     * @param expire 
     *            过期时间 New Date(1000*10)：十秒后过期 
     * @return 
     */  
	@Override
    public boolean add(String key, Object value, Date expire) {  
		key = MD5.crypt(key);
        return Memcached.add(key, value, expire);  
    }  
	
	/** 
     * 仅当缓存中不存在键时，add 命令才会向缓存中添加一个键值对。 
     *  
     * @param key 
     *            键 
     * @param value 
     *            值 
     * @param time 
     *            过期时间 s
     * @return 
     */  
	@Override
    public boolean add(String key, Object value, int time) { 
		key = MD5.crypt(key);
        return Memcached.add(key, value, time);  
    }  
  
    /** 
     * 仅当键已经存在时，replace 命令才会替换缓存中的键。 
     *  
     * @param key 
     *            键 
     * @param value 
     *            值 
     * @return 
     */  
	@Override
    public boolean replace(String key, Object value) {
		key = MD5.crypt(key);
        return Memcached.replace(key, value, null);  
    }  
  
    /** 
     * 仅当键已经存在时，replace 命令才会替换缓存中的键。 
     *  
     * @param key 
     *            键 
     * @param value 
     *            值 
     * @param expire 
     *            过期时间 New Date(1000*10)：十秒后过期 
     * @return 
     */  
	@Override
    public boolean replace(String key, Object value, Date expire) { 
		key = MD5.crypt(key);
        return Memcached.replace(key, value, expire);  
    }  
	
	/** 
     * 仅当键已经存在时，replace 命令才会替换缓存中的键。 
     *  
     * @param key 
     *            键 
     * @param value 
     *            值 
     * @param time 
     *            过期时间 s
     * @return 
     */  
	@Override
    public boolean replace(String key, Object value, int time) {  
		key = MD5.crypt(key);
        return Memcached.replace(key, value, time);  
    }  
  
    /** 
     * get 命令用于检索与之前添加的键值对相关的值。 
     *  
     * @param key 
     *            键 
     * @return 
     */  
	@Override
    public Object get(String key) {  
		key = MD5.crypt(key);
        return Memcached.get(key);  
    }  
  
    /** 
     * 删除 memcached 中的任何现有值。 
     *  
     * @param key 
     *            键 
     * @return 
     */  
	@Override
    public boolean delete(String key) {  
		key = MD5.crypt(key);
        return Memcached.delete(key, null);  
    }  
  
    /** 
     * 删除 memcached 中的任何现有值。 
     *  
     * @param key 
     *            键 
     * @param expire 
     *            过期时间 New Date(1000*10)：十秒后过期 
     * @return 
     */  
	@Override
    public boolean delete(String key, Date expire) {  
		key = MD5.crypt(key);
        return Memcached.delete(key, expire);  
    }  
  
    /** 
     * 清理缓存中的所有键/值对 
     *  
     * @return 
     */  
	@Override
    public boolean flashAll() {  
        return Memcached.flashAll();  
    }  
}  
