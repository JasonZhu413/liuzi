package com.liuzi.easemob;

import com.google.gson.Gson;
import com.liuzi.easemob.comm.ResponseHandler;
import com.liuzi.util.common.LiuziUtil;

import io.swagger.client.ApiException;
import io.swagger.client.api.AuthenticationApi;
import io.swagger.client.api.ChatHistoryApi;
import io.swagger.client.api.ChatRoomsApi;
import io.swagger.client.api.GroupsApi;
import io.swagger.client.api.MessagesApi;
import io.swagger.client.api.UploadAndDownloadFilesApi;
import io.swagger.client.api.UsersApi;
import io.swagger.client.model.Token;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by easemob on 2017/3/14.
 */
public class EasemobConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(EasemobConfig.class);
	
	@Getter @Setter private String org;
	@Getter @Setter private String app;
	@Getter @Setter private String grant_type;
	@Getter @Setter private String client_id;
	@Getter @Setter private String client_secret;
    
	/*@Getter @Setter private ResponseHandler response_handler;
	@Getter @Setter private GroupsApi groups_api;
	@Getter @Setter private ChatHistoryApi history_api;
	@Getter @Setter private ChatRoomsApi rooms_api;
	@Getter @Setter private UploadAndDownloadFilesApi u_d_api;
	@Getter @Setter private UsersApi users_api;
	@Getter @Setter private MessagesApi msg_api;*/
	public static AuthenticationApi api = new AuthenticationApi();
	
	public static String ORG_NAME;
	public static String APP_NAME;
	public static String ACCESS_TOKEN;
	public static Double EXPIREDAT = -1D;
	public static Token body;
	
    public void init() {
    	LiuziUtil.tag("  --------  Liuzi Easymock初始化......  --------");
		
    	/*response_handler = new ResponseHandler();
    	groups_api = new GroupsApi();
    	history_api = new ChatHistoryApi();
    	rooms_api = new ChatRoomsApi();
    	u_d_api = new UploadAndDownloadFilesApi();
    	users_api = new UsersApi();
    	msg_api = new MessagesApi();*/
    	
    	EasemobConfig.ORG_NAME = this.org;
    	EasemobConfig.APP_NAME = this.app;
    	
    	EasemobConfig.body = new Token().clientId(this.client_id)
    			.grantType(this.grant_type)
    			.clientSecret(this.client_secret);
        initTokenByProp();
        
		logger.info("===== easemob初始化完成 ......========");
    }

    @SuppressWarnings("rawtypes")
    public static void initTokenByProp() {
        String resp = null;
        try {
            resp = EasemobConfig.api.orgNameAppNameTokenPost(EasemobConfig.ORG_NAME, 
            		EasemobConfig.APP_NAME, EasemobConfig.body);
        } catch (ApiException e) {
            logger.error(e.getMessage());
        }
        Gson gson = new Gson();
		Map map = gson.fromJson(resp, Map.class);
        EasemobConfig.ACCESS_TOKEN = " Bearer " + map.get("access_token");
        EasemobConfig.EXPIREDAT = System.currentTimeMillis() + (Double) map.get("expires_in");
    }

    /**
     * get Token from memory
     *
     * @return
     */
    public static String getAccessToken() {
        if (EasemobConfig.ACCESS_TOKEN == null || isExpired()) {
            initTokenByProp();
        }
        return EasemobConfig.ACCESS_TOKEN;
    }

    private static Boolean isExpired() {
        return System.currentTimeMillis() > EasemobConfig.EXPIREDAT;
    }

    
}

