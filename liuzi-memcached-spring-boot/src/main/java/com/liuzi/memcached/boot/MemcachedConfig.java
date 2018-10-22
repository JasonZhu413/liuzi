package com.liuzi.memcached.boot;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;
  
/** 
 *  
 * @ClassName: MemcachedUtils 
 * @Description: Memcached工具类 
 * @author yinjw 
 * @date 2014-6-18 下午5:28:08 
 *  
 */  
@Slf4j
@Configuration
public class MemcachedConfig {  
	
	@Value("${memcached.servers}")
    private String[] servers;
    @Value("${memcached.failover}")
    private boolean failover;
    @Value("${memcached.initConn}")
    private int initConn;
    @Value("${memcached.minConn}")
    private int minConn;
    @Value("${memcached.maxConn}")
    private int maxConn;
    @Value("${memcached.maintSleep}")
    private int maintSleep;
    @Value("${memcached.nagle}")
    private boolean nagle;
    @Value("${memcached.socketTO}")
    private int socketTO;
    @Value("${memcached.aliveCheck}")
    private boolean aliveCheck;
    
    @Bean
    public SockIOPool sockIOPool(){
    	log.info("--------  Liuzi Memcached初始化，sockIOPool注入   --------");
    	SockIOPool pool = SockIOPool.getInstance();
        pool.setServers(servers);
        pool.setFailover(failover);
        pool.setInitConn(initConn);
        pool.setMinConn(minConn);
        pool.setMaxConn(maxConn);
        pool.setMaintSleep(maintSleep);
        pool.setNagle(nagle);
        pool.setSocketTO(socketTO);
        pool.setAliveCheck(aliveCheck);
        pool.initialize();
        return pool;
    }

    @Bean
    public MemCachedClient memCachedClient(){
    	log.info("--------  Liuzi Memcached初始化，memCachedClient注入   --------");
        return new MemCachedClient();
    }
}  
