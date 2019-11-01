package com.liuzi.easemob;

import com.google.gson.Gson;
import com.liuzi.easemob.comm.ResponseHandler;
import com.liuzi.util.common.Log;

import io.swagger.client.ApiException;
import io.swagger.client.api.AuthenticationApi;
import io.swagger.client.api.ChatHistoryApi;
import io.swagger.client.api.ChatRoomsApi;
import io.swagger.client.api.GroupsApi;
import io.swagger.client.api.MessagesApi;
import io.swagger.client.api.UploadAndDownloadFilesApi;
import io.swagger.client.api.UsersApi;
import io.swagger.client.model.Token;

import org.springframework.context.annotation.Bean;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by easemob on 2017/3/14.
 */
public class EasemobConfig {
	
	public static AuthenticationApi api = new AuthenticationApi();
	public static Token body;
	
	public static String ORG_NAME;
	public static String APP_NAME;
	public static String ACCESS_TOKEN;
	public static Double EXPIREDAT = -1D;
	
	public EasemobConfig(String org, String app, String clientId, 
			String grantType, String clientSecret){
		ORG_NAME = org;
    	APP_NAME = app;
    	
    	body = new Token().clientId(clientId)
    			.grantType(grantType)
    			.clientSecret(clientSecret);
        initTokenByProp();
        
        Log.info("===== easemob初始化完成 ......========");
	}
	
	@Bean
	public ResponseHandler responseHandler(){
		return new ResponseHandler();
	}
	
	@Bean
	public GroupsApi groupsApi(){
		return new GroupsApi();
	}
	
	@Bean
	public ChatHistoryApi chatHistoryApi(){
		return new ChatHistoryApi();
	}
	
	@Bean
	public ChatRoomsApi chatRoomsApi(){
		return new ChatRoomsApi();
	}
	
	@Bean
	public UploadAndDownloadFilesApi uploadAndDownloadFilesApi(){
		return new UploadAndDownloadFilesApi();
	}
	
	@Bean
	public UsersApi usersApi(){
		return new UsersApi();
	}
	
	@Bean
	public MessagesApi messagesApi(){
		return new MessagesApi();
	}

    @SuppressWarnings("rawtypes")
    public static void initTokenByProp() {
        String resp = null;
        try {
            resp = api.orgNameAppNameTokenPost(ORG_NAME, APP_NAME, body);
        } catch (ApiException e) {
        	Log.error(e, "initTokenByProp error");
        }
        Gson gson = new Gson();
		Map map = gson.fromJson(resp, Map.class);
        ACCESS_TOKEN = " Bearer " + map.get("access_token");
        EXPIREDAT = System.currentTimeMillis() + (Double) map.get("expires_in");
    }

    /**
     * get Token from memory
     *
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

