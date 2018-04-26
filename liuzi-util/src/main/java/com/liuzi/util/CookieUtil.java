package com.liuzi.util;

<<<<<<< HEAD

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

=======
>>>>>>> 02a066291c6197e2d1436dec20fceaf7ded1f342
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;

<<<<<<< HEAD
import com.alibaba.fastjson.JSONObject;

=======
>>>>>>> 02a066291c6197e2d1436dec20fceaf7ded1f342
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
	
<<<<<<< HEAD
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

=======
	public static void set(HttpServletResponse response, String name, String value) {
		set(response, name, value, -1);
	}

	public static void set(HttpServletResponse response, String name, String value, int seconds){
		set(response, name, value, null, -1);
	}

	public static void set(HttpServletResponse response, String name, String value, String domain, int seconds) {
>>>>>>> 02a066291c6197e2d1436dec20fceaf7ded1f342
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

<<<<<<< HEAD
	    try {
			return cookie == null ? null : URLDecoder.decode(cookie.getValue(),"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	    return null;
=======
	    return cookie == null ? null : cookie.getValue();
>>>>>>> 02a066291c6197e2d1436dec20fceaf7ded1f342
	}

	public static void delete(HttpServletResponse response, String name){
		set(response, name, "", 0);
	}
	
}
