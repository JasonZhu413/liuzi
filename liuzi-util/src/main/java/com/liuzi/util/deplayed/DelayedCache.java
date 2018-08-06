package com.liuzi.util.deplayed;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.liuzi.util.executor.ExecutorPool;


public class DelayedCache {

	private static Logger log = LoggerFactory.getLogger(DelayedCache.class);
	
    private final TimeUnit unit = TimeUnit.SECONDS;//超时单位
    
    private static ExecutorPool pool;
    private static int threadMax = 20;
    
    private volatile long firstExpireTime;//首次超时时间
    private volatile DelayQueue<DelayedItem> _Q = new DelayQueue<DelayedItem>();
    //private volatile CacheCallBack expireCallBack;
    
    private static DelayedCache instance;
    
    private DelayedCache(int poolSize) {
    	if(pool == null){
    		pool = ExecutorPool.getInstance(poolSize);
    	}
    }
    
    public static DelayedCache getInstance(){
    	return getInstance(threadMax);
    }
    
    public static DelayedCache getInstance(int poolSize){
    	if(instance == null){
    		poolSize = poolSize == 0 ? threadMax : poolSize;
    		instance = new DelayedCache(poolSize);
    	}
    	return instance;
    }
    
    /**
     * 带有失效回调函数的build方法
     * @return
     */
	public DelayedCache build(long time, String key, 
			Map<String, Object> params, DelayedCallBack callBack) {
		
		this.firstExpireTime = time;
        //this.expireCallBack = callBack;
        
        if(params == null){
        	params = new HashMap<>();
        }
        params.put("delayed_cache_key", key);
        params.put("delayed_cache_time", time);
        
        this.put(key, params);
        
        pool.execute(new Runnable() {
            public void run() {
            	try {
            		expireCheck(callBack);
                } catch (Exception e) {
                	log.error("------ delayed cache error...");
                }
            }
        });
        
        return this;
    }
    

    /**
     * 失效检测
     */
    private void expireCheck(DelayedCallBack callBack) {
    	DelayedCache dc = this;
        for (; ; ) {
            try {
            	DelayedItem delayItem = _Q.take();
                if (delayItem != null) {
                	String key = delayItem.getKey();
                	boolean flag = DelayedCacheMap._CACHE_MAP.get(key) != null;
                    log.info("------ key（" + key + "） expire cache timeout，do handler：" + flag);
                    if(flag){
                    	dc.remove(key);
                    	if (callBack != null) {
                            try {
                            	callBack.handler(delayItem.getParams());
                            } catch (Exception e) {
                            	log.error("------ expire cache callback error", e);
                            }
                        }
                    }
                }
            } catch (InterruptedException e) {
            	log.error("------ expire cache do expire check error", e);
            	
                //失败后停100ms
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                    log.error("------ expire cache sleep", e1);
                }
            }
        }
    }

    public void put(String key, Map<String, Object> params) {
        Object obj = DelayedCacheMap._CACHE_MAP.put(key, _Q);
        if (obj != null) {
        	_Q.remove(new DelayedItem(key, params, 0L));
        }
        
        long milliseconds = TimeUnit.MILLISECONDS.convert(firstExpireTime, unit);
        DelayedItem delayItem = new DelayedItem(key, params, milliseconds);
        _Q.put(delayItem);
        
        log.info("create delayed cache key：" + key + "，time：" + this.firstExpireTime +
        		"，cache map size：" + DelayedCacheMap._CACHE_MAP.size());
    }

    public DelayedItem get(String key) {
        return (DelayedItem) DelayedCacheMap._CACHE_MAP.get(key);
    }

    public Object remove(String key) {
    	Object obj = DelayedCacheMap._CACHE_MAP.remove(key);
        if (obj != null) {
            _Q.remove(new DelayedItem(key, null, 0L));
        }
        
        log.info("------ remove delayed cache key：" + key + "，cache map size：" + 
        		DelayedCacheMap._CACHE_MAP.size());
        
        return obj;
    }
    
    public boolean expire(String key) {
        boolean rs = this.containsKey(key);
        if (rs) {
            long milliseconds = TimeUnit.MILLISECONDS.convert(firstExpireTime, unit);
            DelayedItem delayItem = new DelayedItem(key, null, milliseconds);
            _Q.remove(new DelayedItem(key, null, 0L));
            _Q.put(delayItem);
        }
        return rs;
    }

    public boolean containsKey(String key) {
        return DelayedCacheMap._CACHE_MAP.containsKey(key);
    }
}
