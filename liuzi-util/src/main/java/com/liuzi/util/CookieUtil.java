package com.liuzi.util;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;

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
		set(response, name, value, null, seconds);
	}
	public static void set(HttpServletResponse response, String name, Object obj, String domain, int seconds) {
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

	    Cookie cookie = new Cookie(name, value);
	    cookie.setMaxAge(seconds);
	    cookie.setPath("/");
	    
	    if(!StringUtils.isEmpty(domain)){
	    	cookie.setDomain(domain);
	    }
	    
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

	    try {
			return cookie == null ? null : URLDecoder.decode(cookie.getValue(),"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	    return null;
	}

	public static void delete(HttpServletResponse response, String name){
		set(response, name, "", 0);
	}
	
}
