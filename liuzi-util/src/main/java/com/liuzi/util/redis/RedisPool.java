package com.liuzi.util.redis;

import redis.clients.jedis.JedisPoolConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RedisPool extends JedisPoolConfig{
	
	private String host;
	private int port = 6379;
	private int timeout = 2000;
	private int maxTotal = 8;
	private int maxIdle = 8;
	private int minIdle = 0;
	
	private boolean testOnBorrow = false;
	private boolean testOnReturn = false;
	private boolean jmxEnabled = true;
	
	private boolean blockWhenExhausted = true;
	private long maxWaitMillis = -1;
	
	private boolean testWhileIdle = false;
	private long timeBetweenEvictionRunsMillis = -1;
	private long minEvictableIdleTimeMillis = 1000 * 60 * 30;
	private int numTestsPerEvictionRun = 3;
}
