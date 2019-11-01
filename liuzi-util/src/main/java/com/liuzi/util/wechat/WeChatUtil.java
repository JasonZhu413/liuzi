package com.liuzi.util.wechat;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.util.StringUtils;

import com.liuzi.util.common.Log;
import com.liuzi.util.http.HttpUtil;
import com.liuzi.util.wechat.WeChatEnt.WeChatAccessToken;
import com.liuzi.util.wechat.WeChatEnt.WeChatTicket;
import com.liuzi.util.wechat.WeChatEnt.WeChatUser;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 微信公众号工具类
 * @author zsy
 */
public class WeChatUtil extends WeChatConstants{
	
	private HttpUtil httpUtil;
	
	/**
	 * 微信公众号appid
	 */
	private String appId;
	/**
	 * 微信公众号appsecret
	 */
	private String appSecret;
	/**
	 * 与接口配置信息中的Token要一致
	 */
	private String token;
	
	public WeChatUtil(String appId, String appSecret){
		if(StringUtils.isEmpty(appId) || StringUtils.isEmpty(appSecret)){
			throw new IllegalArgumentException("params is null");
		}
		
		httpUtil = new HttpUtil();
		this.appId = appId;
		this.appSecret = appSecret;
	}
	
    public void setToken(String token) {
		this.token = token;
	}

	/**
     * 生成授权URL
     */
    public String oauth2Url(String redirectUri, WeChatEnt.OauthType type, String state){
    	StringBuilder sb = new StringBuilder();
    	try {
    		sb.append(AUTHORIZE_URL).append("?");
    		sb.append("appid=").append(appId);
    		sb.append("&redirect_uri=").append(URLEncoder.encode(redirectUri, "utf-8"));
    		sb.append("&response_type=").append("code");
    		sb.append("&scope=").append(type.toString());
    		sb.append("&state=").append(state);
    		sb.append("#wechat_redirect");
		} catch (Exception e) {
			Log.error(e, "[WeChat] 生成授权URL错误");
		}
    	return sb.toString();
    }
    
    /**
     *  获取签名算法
     *  https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi
     */
    public WeChatTicket getTicket(String accessToken){
    	StringBuilder sb = new StringBuilder();
    	sb.append(GET_TICKET_URL).append("?");
    	sb.append("access_token=").append(accessToken);
    	sb.append("&type=").append("jsapi");
    	   
    	String result = httpUtil.get(sb.toString());
    	if(StringUtils.isEmpty(result)){
    		return new WeChatTicket();
    	}
    	   
    	JSONObject object = JSONObject.fromObject(result);
    	return new WeChatTicket(object.optString("ticket"), 
    			Integer.valueOf(object.optString("expires_in")));
    }
    
    /**
      * 获取有时效性的access_token（默认2个小时）
      * @return
      */
    public WeChatAccessToken getAccessToken(){
    	StringBuilder sb = new StringBuilder();
        sb.append(TOKEN_URL).append("?");
        sb.append("grant_type=").append("client_credential");
        sb.append("&appid=").append(appId);
        sb.append("&secret=").append(appSecret);
        
        //请求微信服务，获取accessToken
    	String result = httpUtil.get(sb.toString());
    	//返回内容为空，重试
        if(StringUtils.isEmpty(result)){
        	Log.warn("微信获取返回为空");
        	return null;
        }
        
        JSONObject json = JSONObject.fromObject(result);
        Object errorcode = json.get("errorcode");
        if(!StringUtils.isEmpty(errorcode) && !"0".equals(errorcode.toString())){
        	Log.warn(json.toString());
        	return null;
        }
        return (WeChatAccessToken) JSONObject.toBean(json, WeChatAccessToken.class);
    }
    
    /** 
     * 获取access_token
     * @param code
     * @return
     */
    public WeChatAccessToken getAccessToken(String code){
    	StringBuilder sb = new StringBuilder();
        sb.append(ACCESS_TOKEN_URL).append("?");
        sb.append("appid=").append(appId);
        sb.append("&secret=").append(appSecret);
        sb.append("&code=").append(code);
        sb.append("&grant_type=").append("authorization_code");
        
        //请求微信服务，获取accessToken
    	String result = httpUtil.get(sb.toString());
    	//返回内容为空，重试
        if(StringUtils.isEmpty(result)){
        	Log.warn("微信获取返回为空");
        	return null;
        }
        
        JSONObject json = JSONObject.fromObject(result);
        Object errorcode = json.get("errorcode");
        if(!StringUtils.isEmpty(errorcode) && !"0".equals(errorcode.toString())){
        	Log.warn(json.toString());
        	return null;
        }
        return (WeChatAccessToken) JSONObject.toBean(json, WeChatAccessToken.class);
    }
    
    /**
     * 刷新 accessToken
     */
    public WeChatAccessToken refreshToken(String refreshToken){
    	StringBuilder sb = new StringBuilder();  
        sb.append(REFRESH_TOKEN_URL).append("?");
        sb.append("appid=").append(appId); 
        sb.append("&refresh_token=").append(refreshToken);
        sb.append("&grant_type=").append("refresh_token");  
        String result = httpUtil.get(sb.toString());
        if(StringUtils.isEmpty(result)){
        	return new WeChatAccessToken();
        }
        
        JSONObject json = JSONObject.fromObject(result);
		return (WeChatAccessToken) JSONObject.toBean(json, WeChatAccessToken.class);
    }
    
    /** 
     * 判断accesstoken 是否有效 
     * @param accessToken 
     * @param openId 
     * @return 
     */  
    public boolean authAccessToken(String accessToken, String openId){
        StringBuilder sb = new StringBuilder();  
        sb.append(AUTH_URL).append("?");
        sb.append("access_token=").append(accessToken);
        sb.append("&openid=").append(openId);  
        String result = httpUtil.get(sb.toString());
        
        if(StringUtils.isEmpty(result)){
        	return false;
        }
        
        JSONObject json = JSONObject.fromObject(result);
        WeChatResult err = (WeChatResult) JSONObject.toBean(json, WeChatResult.class);
        
        if("0".equals(err.getErrcode())){
        	return true;
        }
        
        return false;  
    } 
    
    /**
     * 发送模板消息
     * @param accessToken token
     * @param temp 模板参数
     * @return
     */
    public WeChatResult messageTemplateSend(String accessToken, 
    		WeChatEnt.MessageTemplate template){
    	if(StringUtils.isEmpty(accessToken)){
    		Log.warn("accessToken为空");
    		return null;
    	}
    	
    	String result = httpUtil.postByJson(MSG_TEMP_SEND_URL + "?access_token=" + accessToken, 
    			template.toString());
    	JSONObject msg = JSONObject.fromObject(result);
    	return (WeChatResult) JSONObject.toBean(msg, WeChatResult.class);
    }
    
    /**
     * 获取用户信息
     * @param accessToken
     * @param openId
     * @return
     */
    public WeChatUser getUserInfo(String accessToken, String openId){
    	StringBuilder sb = new StringBuilder();
    	sb.append(USER_INFO_URL).append("?");
    	sb.append("access_token").append(accessToken);
    	sb.append("&openid").append(openId);
		
		String result = httpUtil.get(sb.toString());
		if(StringUtils.isEmpty(result)){
			 return new WeChatEnt.WeChatUser();
		}
		
		JSONObject json = JSONObject.fromObject(result);
		return (WeChatUser) JSONObject.toBean(json, WeChatUser.class);
    }
    
    /**
     * 获取微信服务器ip地址或ip段
     * 如果公众号基于安全等考虑，需要获知微信服务器的IP地址列表，以便进行相关限制
     */
    public Object[] getWeChatIp(String accessToken){
    	StringBuilder sb = new StringBuilder();
    	sb.append(WECHAT_IP).append("?");
    	sb.append("access_token").append(accessToken);
		
		String result = httpUtil.get(sb.toString());
		if(StringUtils.isEmpty(result)){
			 return new Object[]{};
		}
		JSONObject object = JSONObject.fromObject(result);
		String ipList = object.optString("ip_list");
		if(StringUtils.isEmpty(ipList)){
			 return new Object[]{};
		}
		return JSONArray.fromObject(ipList).toArray();
    }
    
	/**
	 * 校验token验证消息是否来源于微信
	 * 1）将token、timestamp、nonce三个参数进行字典序排序 
	 * 2）将三个参数字符串拼接成一个字符串进行sha1加密 
	 * 3）获得加密后的字符串可与signature对比，标识该请求来源于微信
	 */
    public boolean checkSignature(String signature, String timestamp, String nonce){
    	if(StringUtils.isEmpty(token)){
    		throw new IllegalArgumentException("system token is empty");
    	}
    	
    	String[] arr = new String[] { token, timestamp, nonce };
    	sort(arr);
    	StringBuilder content = new StringBuilder();
        for (int i = 0, j = arr.length; i < j; i++) {
        	content.append(arr[i]);
        }
        MessageDigest md = null;
        String tmpStr = null;

        try {
             md = MessageDigest.getInstance("SHA-1");
             // 将三个参数字符串拼接成一个字符串进行sha1加密
             byte[] digest = md.digest(content.toString().getBytes());
             tmpStr = byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
        	Log.warn(e, "微信TOKEN验证错误");
        }
        content = null;
        // 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
        //return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;
        return tmpStr != null ? tmpStr.equalsIgnoreCase(signature) : false;
    }
    
    /**
     * 将字节数组转换为十六进制字符串
     * @param byteArray
     * @return
     */
    private String byteToStr(byte[] byteArray) {
        String strDigest = "";
        for (int i = 0; i < byteArray.length; i++) {
            strDigest += byteToHexStr(byteArray[i]);
        }
        return strDigest;
    }    
    
    /**
     * 将字节转换为十六进制字符串
     * @param mByte
     * @return
     */
    private String byteToHexStr(byte mByte) {
        char[] tempArr = new char[2];
        tempArr[0] = DIGIT[(mByte >>> 4) & 0X0F];
        tempArr[1] = DIGIT[mByte & 0X0F];
        String s = new String(tempArr);
        return s;
    }
    
    public void sort(String a[]) {
    	int length = a.length;
        for (int i = 0, ii = length - 1; i < ii; i++) {
            for (int j = i + 1, jj = length; j < jj; j++) {
                if (a[j].compareTo(a[i]) < 0) {
                    String temp = a[i];
                    a[i] = a[j];
                    a[j] = temp;
                }
            }
        }
    }
	
	/**
	 * 生成签名
	 * @param args
	 */
	private String create_signature(String jsapi_ticket, String nonce_str, 
			String timestamp, String url){
		StringBuilder sb = new StringBuilder();
		sb.append("jsapi_ticket=").append(jsapi_ticket);
		sb.append("&noncestr=").append(nonce_str);
		sb.append("&timestamp=").append(timestamp);
		sb.append("&url=").append(url);
		
		String sign = sb.toString();
		
		Log.info("[WeChat] 签名：{}", sign);
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
		    crypt.reset();
		    crypt.update(sign.getBytes("UTF-8"));
		    sign = byteToStr(crypt.digest());
		    Log.info("[WeChat] 签名（加密）：{}", sign);
		} catch (Exception e) {
			sign = "";
			Log.error(e, "[WeChat] 签名错误");
		}
		return sign;
	}
}
