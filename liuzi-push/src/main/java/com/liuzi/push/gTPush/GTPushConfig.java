package com.liuzi.push.gTPush;


import java.io.IOException;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.NotificationTemplate;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Configuration
public class GTPushConfig {
	
	@Getter @Setter private String secret;
	
	// id
	public static String id;
	// key
	public static String key;
    // 是否离线
    public static boolean offline = true;
    // 离线有效时间，单位为毫秒，可选
    public static int offlineExpireTime = 24 * 1000 * 3600;
    // 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
    public static int pushNetWorkType = 0;
    
    public GTPushConfig(String id, String key, boolean offline, int offlineExpireTime, 
    		int pushNetWorkType){
    	GTPushConfig.id = id;
    	GTPushConfig.key = key;
    	GTPushConfig.offline = offline;
    	GTPushConfig.offlineExpireTime = offlineExpireTime;
    	GTPushConfig.pushNetWorkType = pushNetWorkType;
    }
    
    @Bean
    public IGtPush iGtPush(){
    	return new IGtPush(key, secret);
    }
    
}
