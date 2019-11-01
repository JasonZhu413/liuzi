package com.liuzi.memcached;


import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.Setter;

import com.liuzi.util.common.Log;
import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;
  
  
public class MemcachedPool{
	
	private static final Pattern PATTERN = Pattern.compile("^.+[:]\\d{1,5}\\s*$");
	protected static final String DEFAULT_NAME = "MEM_POOL_" + System.currentTimeMillis();
	
	@Getter @Setter
	private String poolName = DEFAULT_NAME;
	
	public MemcachedPool(Resource addressConfig, String addressKeyPrefix,
			SockIOPool sockIOPool) throws Exception{
		String[] address = getAddress(addressConfig, addressKeyPrefix);
		StringBuffer sbf = new StringBuffer();
		sbf.append("SockIOPool init, address: ");
		for(String str : address){
			sbf.append(str + ",");
		}
		sbf.deleteCharAt(sbf.length() - 1);
		Log.info("初始化MemcachedPool, {}", sbf.toString());
		sockIOPool.setServers(address);
		sockIOPool.initialize();
	}
	
	@Bean
	public MemCachedClient memCachedClient(){
		Log.info("初始化MemcachedClient, poolName:{}", poolName);
		return new MemCachedClient(poolName);
	}
	
    private String[] getAddress(Resource addressConfig, String addressKeyPrefix) throws Exception {
        try {
            Properties prop = new Properties();
            prop.load(addressConfig.getInputStream());

            List<String> list = new ArrayList<>();
            for (Object key : prop.keySet()) {

                if (!((String) key).startsWith(addressKeyPrefix)) {
                    continue;
                }

                String servers = (String) prop.get(key);
                String[] vals;
                if(StringUtils.isEmpty(servers) || (vals = servers.split(",")).length == 0){
                	throw new IllegalArgumentException("不合法的地址");
                }
                
                for(String val : vals){
                	boolean isIpPort = PATTERN.matcher(val).matches();
                    if (!isIpPort) {
                        throw new IllegalArgumentException("不合法的IP或端口");
                    }
                    list.add(val);
                }
            }

            int size = list.size();
            if(size == 0){
            	throw new IllegalArgumentException("缺少地址");
            }
            String[] address = new String[size];
            address = list.toArray(address);
            
            return address;
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new Exception("解析 memcached 配置文件失败", ex);
        }
    }
}  
