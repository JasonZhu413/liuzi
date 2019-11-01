package com.liuzi.util.token;

import com.liuzi.util.common.Log;
import com.liuzi.util.date.DateUtil;
import com.liuzi.util.encrypt.MD5;
import com.liuzi.util.token.TokenInfo.TokenInfoBuilder;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.minidev.json.JSONObject;

import org.springframework.util.StringUtils;

/**
 * @author JasonZhu
 * @version 1.0
 * @apiNote Token生成/验证
 * @date 2019-05-31 17:47
 **/
public class Token extends TokenConfig{
	
	/**
	 * PC过期时间
	 */
	@Getter @Setter
	private double pcValid = TOKEN_PC_VALID_TIME;
	/**
	 * PC过期时间
	 */
	@Getter @Setter
	private double appValid = TOKEN_APP_VALID_TIME;
	/**
	 * 是否为开发环境，默认true
	 */
	@Getter @Setter
	private boolean debug = TOKEN_DEBUG;
	
	/**
	 * 签名秘钥
	 */
	private String secret = MD5.crypt(TOKEN_SECRET);
	
	public Token(String secret){
		this.secret = MD5.crypt(secret);
	}

    /**
     * 生成token(用户登录成功后调用)
     * @param 存储用户用户信息（如用户id）、token生成时间、token过期时间等自定义字段
     * @return 返回, 失败则返回null
     */
    public String create(Map<String, Object> payload, boolean isApp) {
    	log("----- 生成Token -----");
        if(payload == null){
        	payload = new HashMap<>();
        }
        try {
            //Token生成时间
            long now = System.currentTimeMillis();
            //超时时间
            double time = isApp ? appValid : pcValid;
            //Token过期时间
            long expTime = (long) (now + time * 60 * 1000);
            
            log("当前时间: {}...", getTime(now));
            log("过期时间: {}...", getTime(expTime));
            
            payload.put(TOKEN_CREATE_TIME, now);
            payload.put(TOKEN_EXP_TIME, expTime);

            log("开始生成签名...");
            JSONObject json = new JSONObject(payload);
            JWSObject jwsObject = new JWSObject(HEADER, new Payload(json));
            jwsObject.sign(new MACSigner(secret));
            String token = jwsObject.serialize();
            
            log("----- Token生成成功, Token: {}", token);
            return token;
        } catch (JOSEException e) {
        	error(e, "Token生成错误");
        }
        
        return null;
    }
    
    /**
     * 验证token
     */
    public TokenInfo valid(String token){
    	return valid(token, false);
    }

    /**
     * 验证token
     */
    public TokenInfo valid(String token, boolean isApp) {
    	log("----- 验证Token -----");

    	TokenInfoBuilder builder = TokenInfo.builder()
    			.valid(false)
    			.msg("Token验证失败");
        try {
        	if(StringUtils.isEmpty(token)){
        		log("----- Token验证失败，Token为空 -----");
        		return builder.build();
        	}
        	
        	log("获取验证信息...");
            JWSObject jwsObject = JWSObject.parse(token);
            Payload payload = jwsObject.getPayload();
            JWSVerifier verifier = new MACVerifier(secret);

            log("开始验证...");
            if (jwsObject.verify(verifier)) {
                JSONObject json = payload.toJSONObject();
                
                String newToken = token;
                
                if (json.containsKey(TOKEN_EXP_TIME)) {
                    String exp = json.get(TOKEN_EXP_TIME).toString();
                    //当前时间
                    long curTime = System.currentTimeMillis();
                    //过期时间
                    long expTime = Double.valueOf(exp).longValue();
                    
                    log("当前时间: {}...", getTime(curTime));
                    log("过期时间: {}...", getTime(expTime));
                    
                    //如果当前时间超过token过期时间
                    //并且当前时间超过了最后一次验证时间+有效时间
                    if (curTime > expTime) {
                    	log("----- Token失效 -----");
                    	return builder.data(json).msg("Token失效").build();
                    }
                    
                    //超时时间
                    double time = isApp ? appValid : pcValid;
                    double newExpTime = curTime + time * 60 * 1000;
                    
                    json.put(TOKEN_EXP_TIME, newExpTime);
                    jwsObject = new JWSObject(HEADER, new Payload(json));
                    jwsObject.sign(new MACSigner(secret));
                    newToken = jwsObject.serialize();
                    
                    log("----- Token验证成功，刷新: {}", newToken);
                    
                    builder.valid(true)
                    	.expTime((long) newExpTime)
    					.token(newToken)
    					.data(json)
    					.msg("Token验证成功");
                }
                
                if (json.containsKey(TOKEN_CREATE_TIME)) {
                	String create = json.get(TOKEN_CREATE_TIME).toString();
                	long createTime = Double.valueOf(create).longValue();
                	builder.createTime(createTime);
                }
                
                return builder.build();
            } else {
            	log("----- Token验证失败，校验未通过 -----");
            	return builder.build();
            }
        } catch (Exception e) {
        	error(e, "----- Token验证错误 -----");
        	return builder.build();
        }
    }

    public int getValidTime(boolean isApp){
    	return (int)((isApp ? appValid : pcValid) * 60);
    }
    
    private String getTime(long time){
		return DateUtil.dateToString(new Date(time), DATE_FORMAT);
    }
    
    private void log(String msg, Object... params){
    	if(debug){
    		Log.info(msg, params);
    	}
    }
    
    private void error(Exception e, String msg, Object... params){
    	if(debug){
    		Log.error(e, msg, params);
    	}
    }
    
    public static void main(String[] args) throws Exception{
        //String test = "6zCTO@6zcto.com";
        //String secret = test + MD5.crypt(test);
        //System.out.println("secret: " + secret);
        Token token = new Token("123456");
        Map<String, Object> map = new HashMap<>();
        map.put("userId", "liuzi");
        String getToken = token.create(map, false);
        
        System.out.println("token: " + getToken);
    }
}
