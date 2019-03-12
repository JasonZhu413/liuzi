package com.liuzi.util.token;

import com.liuzi.util.common.DateUtil;
import com.liuzi.util.encrypt.MD5;
import com.liuzi.util.token.TokenInfo.TokenInfoBuilder;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
@Data
@EqualsAndHashCode(callSuper=false)
public class Token extends TokenConfig{
	
	/**
	 * PC过期时间
	 */
	private double pcValid = TOKEN_PC_VALID_TIME;
	/**
	 * PC过期时间
	 */
	private double appValid = TOKEN_APP_VALID_TIME;
	/**
	 * 签名秘钥
	 */
	private String secret = TOKEN_SECRET;
	/**
	 * 是否为开发环境，默认true
	 */
	private boolean debug = TOKEN_DEBUG;
	
	public Token(){}

    /**
     * 生成token(用户登录成功后调用)
     * @param 存储用户用户信息（如用户id）、token生成时间、token过期时间等自定义字段
     * @return 返回, 失败则返回null
     */
    public String create(Map<String, Object> payload, boolean isApp) {
    	tokenLog("----- 生成Token -----");
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
            
            tokenLog("当前时间: " + getTime(now) + "...");
            tokenLog("过期时间: " + getTime(expTime) + "...");
            
            payload.put(TOKEN_CREATE_TIME, now);
            payload.put(TOKEN_EXP_TIME, expTime);

            tokenLog("开始签名...");
            JSONObject json = new JSONObject(payload);
            JWSObject jwsObject = new JWSObject(TokenConfig.HEADER, new Payload(json));
            jwsObject.sign(new MACSigner(secret));
            String token = jwsObject.serialize();
            
            tokenLog("----- Token生成成功, Token: " + token);
            return token;
        } catch (JOSEException e) {
        	tokenLog(3, "----- Token生成失败: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
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
    	tokenLog("----- 验证Token -----");

    	TokenInfoBuilder builder = TokenInfo.builder()
    			.valid(false)
    			.msg("Token验证失败");
        try {
        	if(StringUtils.isEmpty(token)){
        		tokenLog(2, "----- Token验证失败，Token为空 -----");
        		return builder.build();
        	}
        	
        	tokenLog("获取验证信息...");
            JWSObject jwsObject = JWSObject.parse(token);
            Payload payload = jwsObject.getPayload();
            JWSVerifier verifier = new MACVerifier(secret);

            tokenLog("验证...");
            if (jwsObject.verify(verifier)) {
                JSONObject json = payload.toJSONObject();
                
                String newToken = token;
                
                if (json.containsKey(TokenConfig.TOKEN_EXP_TIME)) {
                    String exp = json.get(TokenConfig.TOKEN_EXP_TIME).toString();
                    //当前时间
                    long curTime = System.currentTimeMillis();
                    //过期时间
                    long expTime = Double.valueOf(exp).longValue();
                    
                    tokenLog("当前时间: " + getTime(curTime) + "...");
                    tokenLog("过期时间: " + getTime(expTime) + "...");
                    
                    //如果当前时间超过token过期时间
                    //并且当前时间超过了最后一次验证时间+有效时间
                    if (curTime > expTime) {
                    	tokenLog(2, "----- Token失效 -----");
                    	return builder.msg("Token失效").build();
                    }
                    
                    //超时时间
                    double time = isApp ? appValid : pcValid;
                    double newExpTime = curTime + time * 60 * 1000;
                    
                    json.put(TokenConfig.TOKEN_EXP_TIME, newExpTime);
                    jwsObject = new JWSObject(TokenConfig.HEADER, new Payload(json));
                    jwsObject.sign(new MACSigner(secret));
                    newToken = jwsObject.serialize();
                    
                    tokenLog("----- Token验证成功，刷新: " + newToken);
                    
                    builder.valid(true)
                    	.expTime((long) newExpTime)
    					.token(newToken)
    					.data(json)
    					.msg("Token验证成功");
                }
                
                if (json.containsKey(TokenConfig.TOKEN_CREATE_TIME)) {
                	String create = json.get(TokenConfig.TOKEN_CREATE_TIME).toString();
                	long createTime = Double.valueOf(create).longValue();
                	builder.createTime(createTime);
                }
                
                return builder.build();
            } else {
            	tokenLog(2, "----- Token验证失败，校验未通过 -----");
            	return builder.build();
            }
        } catch (Exception e) {
        	tokenLog(3, "----- Token验证失败：" + e.getMessage());
        	return builder.build();
        }
    }

    public int getValidTime(boolean isApp){
    	return (int)((isApp ? appValid : pcValid) * 60);
    }
    
    private String getTime(long time){
		return DateUtil.date2Str(new Date(time), TokenConfig.DATE_FORMAT);
    }
    
    private void tokenLog(String msg){
    	tokenLog(1, msg);
    }
    
    private void tokenLog(int type, String msg){
    	if(!debug){
    		if(type == 2){
    			log.warn(msg);
    		}else if(type == 3){
    			log.error(msg);
    		}else{
    			log.info(msg);
    		}
    	}
    }
    

    public static void main(String[] args) throws Exception{
        String test = "6zCTO@6zcto.com";
        String secret = test + MD5.crypt(test);
        System.out.println("secret: " + secret);
        
        boolean isApp = false;
        
        Token token = new Token();
        Map<String, Object> map = new HashMap<>();
        map.put("userId", "liuzi");
        String getToken = token.create(map, isApp);
        
        //5秒后验证
        Thread.sleep(1000 * 5);
        TokenInfo result = token.valid(getToken, isApp);
        System.out.println("result1: " + result.toString());
        //5秒后验证
        Thread.sleep(1000 * 5);
        result = token.valid(result.getToken(), isApp);
        System.out.println("result2: " + result.toString());
        //1分钟后验证
        Thread.sleep(1000 * 21);
        result = token.valid(result.getToken(), isApp);
        System.out.println("result3: " + result.toString());
    }
}
