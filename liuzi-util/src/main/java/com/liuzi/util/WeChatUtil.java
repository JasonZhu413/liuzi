package com.liuzi.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;






import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class WeChatUtil {
	
	private final static Logger log = LoggerFactory.getLogger(WeChatUtil.class);
	
    private static String appId;
    private static String appSecret;
    private static String accessTokenUrl;
    private static String apiTicketUrl;

    //微信参数
    private static volatile String accessToken;
    private static volatile String jsApiTicket;
    //获取参数的时刻
    private static volatile Long getTiketTime = 0L;
    private static volatile Long getTokenTime = 0L;
    //参数的有效时间,单位是秒(s)
    private static volatile Long tokenExpireTime = 0L;
    private static volatile Long ticketExpireTime = 0L;
    
    
    private static final String CONFIG_FILE = "conf/application.properties";
    private static String conf_file = CONFIG_FILE;
	private static Properties properties;
	
	
	public WeChatUtil(){
		init();
	}
	
	public WeChatUtil(String filePath){
		if(!StringUtils.isEmpty(filePath)){
			conf_file = filePath;
		}
		init();
	}
	
	public void init(){
		LiuziUtil.tag(" --------  WeChatUtil 配置初始化...... --------");
		
		log.info("===== 加载配置 " + conf_file + " ......========");
		
		if(properties == null){
			try (InputStream in = PropertyUtils.class.getClassLoader().getResourceAsStream(conf_file)){
				properties = new Properties();
				properties.load(in);
			} catch (IOException e) {
				log.error("wechat初始化失败，错误：" + e.getMessage());
				e.printStackTrace();
				return;
			}
		}
		
		appId = properties.getProperty("wechat.appId");
		appSecret = properties.getProperty("wechat.appSecret");
		accessTokenUrl = properties.getProperty("wechat.accessToken.url");
		apiTicketUrl = properties.getProperty("wechat.apiTicket.url");
		
		log.info("===== wechat 加载结束......========");
	}

    public static Map<String, String> getWechatParam(String url){
        //当前时间
        long now = System.currentTimeMillis();

        //判断accessToken是否已经存在或者token是否过期
        if(StringUtils.isBlank(accessToken) || (now - getTokenTime > tokenExpireTime * 1000)){
            JSONObject tokenInfo = getAccessToken();
            if(tokenInfo != null){
                log.info("tokenInfo====>"+tokenInfo.toJSONString());
                accessToken = tokenInfo.getString("access_token");
                tokenExpireTime = tokenInfo.getLongValue("expires_in");
                //获取token的时间
                getTokenTime = System.currentTimeMillis();
                log.info("accessToken：" + accessToken + "，tokenExpireTime：" + tokenExpireTime + "s，getTokenTime：" + getTokenTime+"ms");
            }else{
                log.info("tokenInfo is null，failure of getting tokenInfo,please do some check~");
            }
        }

        //判断jsApiTicket是否已经存在或者是否过期
        if(StringUtils.isBlank(jsApiTicket) || (now - getTiketTime > ticketExpireTime*1000)){
            JSONObject ticketInfo = getJsApiTicket();
            if(ticketInfo != null){
                jsApiTicket = ticketInfo.getString("ticket");
                ticketExpireTime = ticketInfo.getLongValue("expires_in");
                getTiketTime = System.currentTimeMillis();
                log.info("jsApiTicket：" + jsApiTicket + "，ticketExpireTime：" + ticketExpireTime + "s，getTiketTime：" + getTiketTime + "ms");
            }else{
                log.info("ticketInfo is null，failure of getting tokenInfo,please do some check");
            }
        }

        //生成微信权限验证的参数
        return makeWXTicket(jsApiTicket, url);
    }

    //获取accessToken
    private static JSONObject getAccessToken(){
        //String accessTokenUrl = https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
        String requestUrl = accessTokenUrl.replace("APPID", appId).replace("APPSECRET", appSecret);
        String result = HttpUtil.get(requestUrl);
        return JSONObject.parseObject(result);
    }

    //获取ticket
    private static JSONObject getJsApiTicket(){
        //String apiTicketUrl = https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi
        String requestUrl = apiTicketUrl.replace("ACCESS_TOKEN", accessToken);
        String result = HttpUtil.get(requestUrl);
        return JSONObject.parseObject(result);
    }

    //生成微信权限验证的参数
    public static Map<String, String> makeWXTicket(String jsApiTicket, String url) {
        Map<String, String> ret = new HashMap<String, String>();
        String nonceStr = createNonceStr();
        String timestamp = createTimestamp();
        String string1;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsApiTicket +
                "&noncestr=" + nonceStr +
                "&timestamp=" + timestamp +
                "&url=" + url;
        log.info(string1);
        try{
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        }catch (NoSuchAlgorithmException e){
            log.error(e.getMessage(), e);
        }catch (UnsupportedEncodingException e){
            log.error(e.getMessage(), e);
        }

        ret.put("url", url);
        ret.put("jsapi_ticket", jsApiTicket);
        ret.put("nonceStr", nonceStr);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);
        ret.put("appid", appId);

        return ret;
    }
    //字节数组转换为十六进制字符串
    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash){
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
    //生成随机字符串
    private static String createNonceStr() {
        return UUID.randomUUID().toString();
    }
    
    //生成时间戳
    private static String createTimestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
}
