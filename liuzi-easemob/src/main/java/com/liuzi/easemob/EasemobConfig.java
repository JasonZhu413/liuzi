package com.liuzi.easemob;

import com.google.gson.Gson;
import com.liuzi.util.LiuziUtil;

import io.swagger.client.ApiException;
import io.swagger.client.api.AuthenticationApi;
import io.swagger.client.model.Token;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * Created by easemob on 2017/3/14.
 */
public class EasemobConfig {
	
	private final static String CONFIG_FILE = "conf/easemob.properties";
	private static String g_conf_file = CONFIG_FILE;
	private static Properties properties;
	
	public static String ORG_NAME;
	public static String APP_NAME;
	
    public static String GRANT_TYPE;
    private static String CLIENT_ID;
    private static String CLIENT_SECRET;
    private static Token BODY;
    private static AuthenticationApi API = new AuthenticationApi();
    private static String ACCESS_TOKEN;
    private static Double EXPIREDAT = -1D;
    private static final Logger logger = LoggerFactory.getLogger(EasemobConfig.class);

    public EasemobConfig(){
    	init();
    }
    
    public EasemobConfig(String confFile) {
    	if(!StringUtils.isEmpty(confFile)){
			g_conf_file = confFile;
		}
    	init();
    } 
    
    /**
     * get token from server
     */
    private void init() {
    	LiuziUtil.tag("  --------  Liuzi Easymock初始化......  --------");
		
		logger.info("===== easemob初始化，加载配置 " + g_conf_file + " ......========");
		
		if(properties == null){
			try (InputStream in = PropertyUtils.class.getClassLoader().getResourceAsStream(g_conf_file)){
				properties = new Properties();
				properties.load(in);
			} catch (IOException e) {
				logger.error("easemob初始化失败，错误：" + e.getMessage());
				e.printStackTrace();
				return;
			}
		}
		
		GRANT_TYPE = properties.getProperty("easemob.grant_type");
        CLIENT_ID = properties.getProperty("easemob.client.id");
        CLIENT_SECRET = properties.getProperty("easemob.client.secret");
        
        ORG_NAME = properties.getProperty("easemob.org");
        APP_NAME = properties.getProperty("easemob.app");
		
        BODY = new Token().clientId(CLIENT_ID).grantType(GRANT_TYPE).clientSecret(CLIENT_SECRET);
        initTokenByProp();
        
		logger.info("===== easemob初始化完成 ......========");
    }

    public static void initTokenByProp() {
        String resp = null;
        try {
            resp = API.orgNameAppNameTokenPost(ORG_NAME, APP_NAME, BODY);
        } catch (ApiException e) {
            logger.error(e.getMessage());
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

