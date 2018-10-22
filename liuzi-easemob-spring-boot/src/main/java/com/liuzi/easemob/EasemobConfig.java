package com.liuzi.easemob;

import com.google.gson.Gson;
import com.liuzi.util.LiuziUtil;

import io.swagger.client.ApiException;
import io.swagger.client.api.AuthenticationApi;
import io.swagger.client.model.Token;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by easemob on 2017/3/14.
 */
@Slf4j
@Configuration
public class EasemobConfig {
	
    @Value("${easemob.org}")
	public static String ORG_NAME;
    @Value("${easemob.app}")
	public static String APP_NAME;
	@Value("${easemob.grant_type}")
    public static String GRANT_TYPE;
	@Value("${easemob.client.id}")
    private static String CLIENT_ID;
	@Value("${easemob.client.secret}")
    private static String CLIENT_SECRET;
	
    private static Token BODY;
    private static AuthenticationApi API = new AuthenticationApi();
    private static String ACCESS_TOKEN;
    private static Double EXPIREDAT = -1D;

    public EasemobConfig() {
    	LiuziUtil.tag("  --------  Liuzi Easymock初始化......  --------");
		
        BODY = new Token().clientId(CLIENT_ID).grantType(GRANT_TYPE).clientSecret(CLIENT_SECRET);
        initTokenByProp();
        
        log.info("===== easemob初始化完成 ......========");
    }

    public static void initTokenByProp() {
        String resp = null;
        try {
            resp = API.orgNameAppNameTokenPost(ORG_NAME, APP_NAME, BODY);
        } catch (ApiException e) {
        	log.error(e.getMessage());
        }
        Gson gson = new Gson();
        
        Map map = gson.fromJson(resp, Map.class);
        ACCESS_TOKEN = " Bearer " + map.get("access_token");
        EXPIREDAT = System.currentTimeMillis() + (Double) map.get("expires_in");
    }

    /**
     * get Token from memory
     * @return
     */
    public static String getAccessToken() {
        if (ACCESS_TOKEN == null || isExpired()) {
            initTokenByProp();
        }
        return ACCESS_TOKEN;
    }

    private static Boolean isExpired() {
        return System.currentTimeMillis() > EXPIREDAT;
    }

}

