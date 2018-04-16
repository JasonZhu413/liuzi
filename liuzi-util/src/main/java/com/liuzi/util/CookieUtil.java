package com.liuzi.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;

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
	
	public static void set(HttpServletResponse response, String name, String value) {
		set(response, name, value, -1);
	}

	public static void set(HttpServletResponse response, String name, String value, int seconds){
		set(response, name, value, null, -1);
	}

	public static void set(HttpServletResponse response, String name, String value, String domain, int seconds) {
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

	    return cookie == null ? null : cookie.getValue();
	}

	public static void delete(HttpServletResponse response, String name){
		set(response, name, "", 0);
	}
	
}
