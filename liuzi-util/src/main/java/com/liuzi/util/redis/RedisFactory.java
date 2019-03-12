package com.liuzi.util.redis;


import redis.clients.jedis.JedisPool;




public class RedisFactory extends JedisPool{
	
	/*public RedisFactory(RedisPool redisPool){
        super(redisPool, redisPool.getHost(), redisPool.getPort(),
        		redisPool.getTimeout());
	}*/
	
	/**
	 * redis初始化
	 * @param path 配置文件地址
	 * redis.host host地址
	 * redis.port 端口
	 * redis.timeout 连接redis超时时间
	 * redis.pool.maxTotal 最大连接数, 默认值8
	 * redis.pool.maxIdle 允许最大空闲的连接数, 默认值8
	 * redis.pool.minIdle 确保最少空闲的连接数, 默认值0
	 * 
	 * redis.pool.testOnBorrow 使用连接时是否做连接有效性检测(ping), 无效连接会被移除, 默认值false, 业务量大时候建议设置false(多一次ping的开销)
	 * redis.pool.testOnReturn 归还连接时是否做连接有效性检测(ping)，无效连接会被移除, 默认值false, 业务量大时候建议设置false(多一次ping的开销)
	 * redis.pool.jmxEnabled 是否开启jmx监控, 可用于监控, 默认值true, 建议开启
	 * 
	 * redis.pool.blockWhenExhausted 连接用尽, 调用者是否要等待, 默认值true(maxWaitMillis生效), 建议设置true
	 * redis.pool.maxWaitMillis 连接用尽, 调用者的最大等待时间(毫秒), 默认值-1(永不超时)
	 * 
	 * redis.pool.testWhileIdle 是否开启空闲Jedis对象检测(maxWaitMillis生效), 默认值false, 建议值 true
	 * redis.pool.timeBetweenEvictionRunsMillis 空闲资源的检测周期(毫秒), 默认值 -1
	 * redis.pool.minEvictableIdleTimeMillis 资源池中资源最小空闲时间(毫秒), 达到此值后空闲资源将被移除, 默认值1000*60*30=30分钟
	 * redis.pool.numTestsPerEvictionRun 空闲资源检测时, 每次的采样数, 默认值3(设置为-1, 是对所有连接做空闲监测)
	 */
	/*public JedisFactory(RedisPool redisPool){
		try {
			prop = PropertiesUtil.prop(redis_pro_path);
			if(prop == null){
				return jedisPool;
			}
			
			String host = prop.getString("redis.host");
			if(StringUtils.isEmpty(host)){
				log.warn("redis host is null");
				return null;
			}
			int port = prop.getInt("redis.port", 6379);
			int timeout = prop.getInt("redis.timeout", 2000);
			
			int maxTotal = prop.getInt("redis.pool.maxTotal", 8);
			int maxIdle = prop.getInt("redis.pool.maxIdle", 8);
			int minIdle = prop.getInt("redis.pool.minIdle", 0);
			
			boolean blockWhenExhausted = prop.getBoolean("redis.pool.blockWhenExhausted", true);
			int maxWaitMillis = prop.getInt("redis.pool.maxWaitMillis", -1);
			
			boolean testOnBorrow = prop.getBoolean("redis.pool.testOnBorrow", false);
			boolean testOnReturn = prop.getBoolean("redis.pool.testOnReturn", false);
			 
			boolean jmxEnabled = prop.getBoolean("redis.pool.jmxEnabled", true);
			
			boolean testWhileIdle = prop.getBoolean("redis.pool.testWhileIdle", false);
			
			int timeBetweenEvictionRunsMillis = prop.getInt("redis.pool.timeBetweenEvictionRunsMillis", -1);
			int minEvictableIdleTimeMillis = prop.getInt("redis.pool.minEvictableIdleTimeMillis", 1000 * 60 * 30);
			int numTestsPerEvictionRun = prop.getInt("redis.pool.numTestsPerEvictionRun", 3);
			
			jedisPool = jedisPool(host, port, timeout, maxTotal, maxIdle, minIdle, blockWhenExhausted, 
					maxWaitMillis, testOnBorrow, testOnReturn, jmxEnabled, testWhileIdle, 
					timeBetweenEvictionRunsMillis, minEvictableIdleTimeMillis, numTestsPerEvictionRun);
        } catch (Exception e) {
            log.error("create JedisPool error : " + e);
        }
		return jedisPool;
    }*/
	/*public JedisPool jedisPool(String host, int port, int timeout, int maxTotal, int maxIdle, 
			int minIdle, boolean blockWhenExhausted, int maxWaitMillis, boolean testOnBorrow,
			boolean testOnReturn, boolean jmxEnabled, boolean testWhileIdle, int timeBetweenEvictionRunsMillis,
			int minEvictableIdleTimeMillis, int numTestsPerEvictionRun){
		try{
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(maxTotal);
            config.setMaxIdle(maxIdle);
            config.setMinIdle(minIdle);
            config.setTestOnBorrow(testOnBorrow);
            config.setTestOnReturn(testOnReturn);
            config.setJmxEnabled(jmxEnabled);
            
            config.setBlockWhenExhausted(blockWhenExhausted);
            config.setMaxWaitMillis(maxWaitMillis);
            
            config.setTestWhileIdle(testWhileIdle);
			config.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
            config.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
            config.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
            
            jedisPool = new JedisPool(config, host, port, timeout);
        } catch (Exception e) {
            log.error("create JedisPool error : " + e);
        }
        log.info("=====初始化redis池成功!");
        
        return jedisPool;
    }
	
	public static Jedis getJedis(){
		if(jedisPool == null){
			throw new NullPointerException("jedis is not init, pool is empty!"); 
		}
		return jedisPool.getResource();
	}
	
	public static void release(Jedis jedis){
		if(jedis != null){
			jedis.close();
		} 
	}*/
}


