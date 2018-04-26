package com.liuzi.util.deplayed;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 
 * Cache<String, String> cache = new Cache<String, String>(time, TimeUnit.SECONDS);//超时时间, 时间单位
    cache.setExpireTime(1, true);// 检查时间间隔, 超时后是否连续检查  
    cache.setCheckTimes(5);//超时次数移除，true才有效
    cache.build(new CacheCallBack<Object, Object>(){//超时回调
        @Override
        public void handler(Object k, boolean isEnd) throws Exception {  
            System.out.println("元素:[" + k + "]超时,超时是否删除：" + isEnd); 
            System.out.println("订单超时未支付，删除id为"+item+"的订单...");
            cache.remove(key.toString());
            redisService.hdel(key, item);
        }
    });  
 * 
 * 	//超时时间, 时间单位
 * new Cache<String, String>(time, TimeUnit.SECONDS)
 * //下次检查时间间隔(check为true才生效) , 超时后是否继续检查，false:失败后直接从队列中移除，true: 超时不移除，继续检查，和第二个参数配合
 *  setExpireTime(long subsequentExpireTime, boolean check)//默认间隔1秒，超时后继续一处
	//设置共检查多少次，当设置连续检查为true时才生效。当检查到达该次数后会从队列中移除。
	setCheckTimes(Integer times) //默认4次
	//创建的时候可以传一个回调函数，每次元素失效检查的时候都会触发该回调方法。
	build(ExpireCallBack<K, V> callBack) 
	//当不需要回调的时候可以使用该方法创建。
	build()
	//添加元素，有则更新，同时会重新计算超时时间。
	put(K key, V value)
	//获取元素
	V get(K key)
	//判断是否存在该元素
	boolean containsKey(K key)
	//更新超时时间，会重新计算超时。
	boolean expire(K key)
 */
public class DelayedCache<K, V> {

	private static Logger log = LoggerFactory.getLogger(DelayedCache.class);
	
    //Data Cache
    private ConcurrentHashMap<K, V> _CACHE_MAP = new ConcurrentHashMap<K, V>();

    //DelayQueue for expire
    private DelayQueue<DelayedItem<K>> _Q = new DelayQueue<DelayedItem<K>>();

    private Thread expireCheckThread;

    //首次超时时间
    private long firstExpireTime;

    //超时之后是否删除(如果超时后继续,则把该对象再次放入cache,每隔subsequentExpireTime时间检查一次,直到 checkTimes 次数)
    private boolean check = false;
    
    public void setCheck(boolean check) {
		this.check = check;
	}

	//第一次超时之后，后续超时时间
    private long subsequentExpireTime = 1;

    //超时单位
    private TimeUnit unit = TimeUnit.SECONDS;

    //总共检查次数
    private Integer checkTimes = 4;

    private CacheCallBack<K, V> expireCallBack;

    public DelayedCache(long firstExpireTime) {
        this.firstExpireTime = firstExpireTime;
    }
    
    public DelayedCache(long firstExpireTime, TimeUnit unit) {
        this.firstExpireTime = firstExpireTime;
        this.unit = unit;
    }
    
    public DelayedCache<K, V> setExpireTime() {
        return this;
    }
    
    public DelayedCache<K, V> setExpireTime(boolean check) {
    	this.check = check;
        return this;
    }

    public DelayedCache<K, V> setExpireTime(long subsequentExpireTime, boolean check) {
        //Cache expireCache = new Cache(firstExpireTime, unit);
        if (check == true) {
            this.check = check;
            if (subsequentExpireTime <= 0) {
            	this.subsequentExpireTime = firstExpireTime;
            } else {
            	this.subsequentExpireTime = subsequentExpireTime;
            }
        }
        return this;
    }
    
    public DelayedCache<K, V> setCheckTimes() {
        return this;
    }

    public DelayedCache<K, V> setCheckTimes(Integer times) {
        if (times != null && times > 0) {
            this.checkTimes = times - 1;
        }
        return this;
    }

    /**
     * 采用这种方式，而不是在构造方法中创建线程是为了防止构造方法中启动线程，且线程中会有实例引用导致的this逸出
     * <p>下面带参数的build方法原因也是如此
     *
     * @return
     */
    public DelayedCache<K, V> build() {
        Runnable daemonTask = new Runnable() {
            public void run() {
                expireCheck();
            }
        };
        expireCheckThread = new Thread(daemonTask);
        expireCheckThread.setDaemon(true);
        expireCheckThread.setName("ExpireCacheCheckThread");
        expireCheckThread.start();
        return this;
    }

    /**
     * 带有失效回调函数的build方法
     *
     * @param callBack
     * @return
     */
    public DelayedCache<K, V> build(CacheCallBack callBack) {
        this.expireCallBack = callBack;
        Runnable daemonTask = new Runnable() {
            public void run() {
                expireCheck();
            }
        };
        expireCheckThread = new Thread(daemonTask);
        expireCheckThread.setDaemon(true);
        expireCheckThread.setName("ExpireCacheCheckThread");
        expireCheckThread.start();
        return this;
    }

    /**
     * 真正的失效检测
     */
    private void expireCheck() {
        for (; ; ) {
            try {
                DelayedItem<K> delayItem = _Q.take();
                log.info("expire check cache delayItem：" + delayItem);
                if (delayItem != null) {
                    log.warn("[expireCache] timeout");
                    if (expireCallBack != null) {
                        try {
                            expireCallBack.handler(delayItem.getItem(), delayItem.isEnd());
                        } catch (Exception e) {
                        	log.error("[ExpireCache expireCheck] callback error", e);
                        }
                    }

                    //如果超时后还继续检测，则item设置新的超时时间;并且没有超过总检查次数
                    //否则从Cache中删除数据
                    if (check && !delayItem.isEnd()) {
                        long milliseconds = TimeUnit.MILLISECONDS.convert(subsequentExpireTime, unit);
                        delayItem.setMilliseconds(milliseconds);
                        if (delayItem.getCheckTimesLeft() != null) {
                            delayItem.setCheckTimesLeft(delayItem.getCheckTimesLeft().intValue() - 1);
                        }
                        _Q.put(delayItem);
                    } else {
                        _CACHE_MAP.remove(delayItem.getItem());
                    }
                }
            } catch (InterruptedException e) {
            	log.error("[Expire Cache] do expireCheckError", e);

                //失败后停100ms
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public void put(K key, V value) {
    	log.info("create cache key：" + key);
        V oldValue = _CACHE_MAP.put(key, value);
        if (oldValue != null) {
            //todo 这个地方性能比较差，DelayQueue删除元素慢
            boolean result = _Q.remove(new DelayedItem<K>(key, 0L, 0));
        }

        long milliseconds = TimeUnit.MILLISECONDS.convert(firstExpireTime, unit);
        DelayedItem<K> delayItem = new DelayedItem<K>(key, milliseconds, checkTimes);
        _Q.put(delayItem);
    }

    public boolean expire(K key) {
        boolean rs = _CACHE_MAP.containsKey(key);
        if (rs) {
            long milliseconds = TimeUnit.MILLISECONDS.convert(firstExpireTime, unit);
            DelayedItem<K> delayItem = new DelayedItem<K>(key, milliseconds, checkTimes);
            _Q.remove(new DelayedItem<K>(key, 0L, 0));
            _Q.put(delayItem);
        }
        return rs;
    }

    public V get(K key) {
        return _CACHE_MAP.get(key);
    }

    public V remove(K key) {
    	log.info("cache remove key：" + key);
        V value = _CACHE_MAP.remove(key);
        if (value != null) {
            _Q.remove(new DelayedItem<K>(key, 0L, 0));
        }
        return value;
    }

    public boolean containsKey(K key) {
        return _CACHE_MAP.containsKey(key);
    }

}
