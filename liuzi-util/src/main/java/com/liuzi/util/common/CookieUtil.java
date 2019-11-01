package com.liuzi.util.common;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;



import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

/**
 * @Title:        CookieUtil
 * 
 * @Description:  TODO
 * 
 * @author        ZhuShiyao
 * 
 * @Date          2017年3月30日 下午11:19:15
 * 
 * @version       1.0
 * 
 */
public class CookieUtil {
	
	public static void set(HttpServletResponse response, String name, Object value) {
		set(response, name, value, -1);
	}
	public static void set(HttpServletResponse response, String name, Object value, int seconds){
		set(response, name, value, seconds, null);
	}
	public static void set(HttpServletResponse response, String name, Object obj, int seconds, String domain) {
		String value = null;
		try {
			if(!StringUtils.isEmpty(obj)){
				if(obj instanceof String){
					value = obj.toString();
				}else{
					value = URLEncoder.encode(JSONObject.toJSONString(obj), "utf-8");
				}
				
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		set(response, name, value, seconds, domain);
	}
	public static void set(HttpServletResponse response, String name, String value) {
		set(response, name, value, -1);
	}

	public static void set(HttpServletResponse response, String name, String value, int seconds){
		set(response, name, value, seconds, null);
	}

	public static void set(HttpServletResponse response, String name, String value, int seconds, String domain) {
		set(response, name, value, seconds, null, domain);
	}
	
	public static void set(HttpServletResponse response, String name, String value, int seconds, 
			String path, String domain) {
		path = StringUtils.isEmpty(path) ? "/" : path;
		
	    Cookie cookie = new Cookie(name, value);
	    cookie.setMaxAge(seconds);
	    cookie.setPath(path);
	    //cookie.setHttpOnly(true);//为了防止XSS攻击，设置此属性js无法获取cookie
	    if(!StringUtils.isEmpty(domain)){
	    	cookie.setDomain(domain);
	    }
	    //log.info("cookie set: " + cookie.toString());
	    response.addCookie(cookie);
	}
	
	
	
	public static String get(HttpServletRequest request, String key){
		if(StringUtils.isEmpty(key)) return null;
		
	    Cookie cookie = null;
	    
	    Cookie[] cookies = request.getCookies();
	    if (cookies != null){
	    	for(Cookie ck : cookies){
	    		if (key.equals(ck.getName())){
	    			cookie = ck;
	    			break;
	    		}
	    	}
	    }
	    
	    String value = null;
	    if(cookie != null && (value = cookie.getValue()) != null){
	    	try {
	    		value = URLDecoder.decode(value, "utf-8");
			} catch (UnsupportedEncodingException e) {
				Log.error(e, "Cookie内容编码转换错误：{}" + value);
			}
	    }
	    return value;
	}
	
	public static void delete(HttpServletRequest request, HttpServletResponse response, String name){
		delete(request, response, name, "/");
	}

	public static void delete(HttpServletRequest request, HttpServletResponse response, 
			String name, String path){
		if(StringUtils.isEmpty(name)){
	    	return;
	    }
		
		path = StringUtils.isEmpty(path) ? "/" : path;
		
		Cookie cookie = null;
	    
	    Cookie[] cookies = request.getCookies();
	    if (cookies != null){
	    	for(Cookie ck : cookies){
	    		if (name.equals(ck.getName())){
	    			cookie = ck;
	    			break;
	    		}
	    	}
	    }
	
	    if(cookie == null){
	    	return;
	    }
	    
	    cookie.setValue(null);
	    cookie.setMaxAge(0);
	    cookie.setPath(path);
	    //cookie.setHttpOnly(true);
	    
	    //log.info("cookie delete: " + cookie.toString());
	    response.addCookie(cookie);
	}
	
}
