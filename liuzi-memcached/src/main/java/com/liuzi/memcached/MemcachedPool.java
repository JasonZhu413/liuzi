package com.liuzi.memcached;


import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import org.springframework.core.io.Resource;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import com.whalin.MemCached.SockIOPool;
  
/** 
 */  
@Slf4j
public class MemcachedPool extends SockIOPool{
	
	@Getter @Setter private Resource addressConfig;
	@Getter @Setter private String addressKeyPrefix;
	
	private Pattern p = Pattern.compile("^.+[:]\\d{1,5}\\s*$");

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

                boolean isIpPort = p.matcher(val).matches();
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
    
    public void afterPropertiesSet(SockIOPool sockIOPool) throws Exception {
    	String[] address = this.getAddress();
    	
		sockIOPool.setServers(address);
		sockIOPool.initialize();
		log.info("SockIOPool initialize");
    }
  
    public static void main(String[] args) {
    	String cats[] = new String[]{};
    	cats[0] = "abc";
    	System.out.println(cats);
	}
}  
