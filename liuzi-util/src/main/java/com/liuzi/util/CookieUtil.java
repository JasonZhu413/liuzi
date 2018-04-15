package com.liuzi.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	public static String getCookie(HttpServletRequest request, String key){
	    String value = null;
	    Cookie cookie = null;
	    Cookie[] cookies = request.getCookies();
	    if (cookies != null){
	    	int i = 0;

	    	while (i < cookies.length){
	    		if (cookies[i].getName().equals(key)){
	    			cookie = cookies[i];
	    			break;
	    		}
	    		++i;
	    	}
	    }

	    if (cookie != null){
	    	value = cookie.getValue();
	    }
	      
	    if (value == null){
	    	value = "";
	    }
	      
	    return value;
	}

	public static void setCookie(HttpServletResponse response, String name, String value, int seconds){
	    Cookie cookie = new Cookie(name, value);
	    cookie.setMaxAge(seconds);
	    cookie.setPath("/");
	    //cookie.setDomain(ConfigUtil.getStringValue("cookie.domain"));
	    response.addCookie(cookie);
	}

	public static void setCookie(HttpServletResponse response, String name, String value, String domain, int seconds) {
	    Cookie cookie = new Cookie(name, value);
	    cookie.setMaxAge(seconds);
	    cookie.setPath("/");
	    cookie.setDomain(domain);
	    response.addCookie(cookie);
	}

	public static void setCookie(HttpServletResponse response, String name, String value) {
	    Cookie cookie = new Cookie(name, value);
	    cookie.setPath("/");

	    response.addCookie(cookie);
	}

	public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String cookieName){
	    Cookie cookie = new Cookie(cookieName, "");

	    cookie.setMaxAge(0);

	    cookie.setPath("/");
	    response.addCookie(cookie);
	}
	
}
