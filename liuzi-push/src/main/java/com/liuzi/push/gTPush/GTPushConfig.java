package com.liuzi.push.gTPush;




import org.springframework.context.annotation.Bean;

import com.gexin.rp.sdk.http.IGtPush;

import lombok.Data;

@Data
public class GTPushConfig {
	private static final boolean OFFLINE = true;
	private static final int OFFLINE_EXPIRE_TIME = 24 * 1000 * 3600;
	private static final int PUSH_NET_WORK_TYPE = 0;
	
    // 是否离线
	public static boolean offline = OFFLINE;
    // 离线有效时间，单位为毫秒，可选
	public static int offlineExpireTime = OFFLINE_EXPIRE_TIME;
    // 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
	public static int pushNetWorkType = PUSH_NET_WORK_TYPE;
    
    public static String appId;
	// key
    public static String appKey;
    
    private String masterSecret;
    private boolean useSSL = false;
    
    public GTPushConfig(String appId, String appKey){
    	this(appId, appKey, offline, offlineExpireTime, pushNetWorkType);
    }
    
    public GTPushConfig(String appId, String appKey, boolean offline, 
    		int offlineExpireTime, int pushNetWorkType){
    	GTPushConfig.appId = appId;
    	GTPushConfig.appKey = appKey;
    	GTPushConfig.offline = offline;
    	GTPushConfig.offlineExpireTime = offlineExpireTime;
    	GTPushConfig.pushNetWorkType = pushNetWorkType;
    }
    
    @Bean
    public IGtPush iGtPush(){
    	return new IGtPush(appKey, masterSecret, useSSL);
    }
    
}
