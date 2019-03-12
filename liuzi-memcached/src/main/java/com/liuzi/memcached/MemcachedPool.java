package com.liuzi.memcached;


import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import org.springframework.core.io.Resource;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import com.whalin.MemCached.SockIOPool;
  
  
@Slf4j
@Data
@EqualsAndHashCode(callSuper=false)
public class MemcachedPool extends SockIOPool{
	
	private static final Pattern PATTERN = Pattern.compile("^.+[:]\\d{1,5}\\s*$");
	
	private Resource addressConfig;
	private String addressKeyPrefix;
	
    public void afterPropertiesSet(SockIOPool sockIOPool) throws Exception {
    	String[] address = this.getAddress();
    	
		sockIOPool.setServers(address);
		sockIOPool.initialize();
		log.info("SockIOPool initialize");
    }
    
    private String[] getAddress() throws Exception {
        try {
            Properties prop = new Properties();
            prop.load(this.addressConfig.getInputStream());

            List<String> list = new ArrayList<>();
            for (Object key : prop.keySet()) {

                if (!((String) key).startsWith(addressKeyPrefix)) {
                    continue;
                }

                String val = (String) prop.get(key);

                boolean isIpPort = PATTERN.matcher(val).matches();
                if (!isIpPort) {
                    throw new IllegalArgumentException("ip或 port不合法");
                }
                list.add(val);
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
