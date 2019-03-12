package com.liuzi.util.cookie;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class Cookie {
	
	private HttpServletRequest request;
	
	private HttpServletResponse response;
	
	private String comment;
	
	private String domain;
	
	private Boolean httpOnly;
	
	private Integer maxAge;
	
	private String path;

	private Boolean secure;
	
    private Integer version;
    
    private String name;
    
    private String value;
    
    public boolean add(){
    	if(StringUtils.isEmpty(name) || StringUtils.isEmpty(value))
    		return false;
    	
    	path = StringUtils.isEmpty(path) ? "/" : path;
    	maxAge = maxAge == null ? -1 : maxAge;
    	secure = secure == null ? false : secure;
    	version = version == null ? 0 : version;
    	
    	javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie(name, value);
    	cookie.setHttpOnly(true);
    	cookie.setPath(path);
	    cookie.setMaxAge(maxAge);
	    cookie.setSecure(secure);
	    cookie.setVersion(version);
	    if(!StringUtils.isEmpty(domain)){
	    	cookie.setDomain(domain);
	    }
	    response.addCookie(cookie);
	    return true;
    }
	
    public String get(){
    	if(StringUtils.isEmpty(name)) 
    		return null;
		
    	javax.servlet.http.Cookie cookie = null;
	    
    	javax.servlet.http.Cookie[] cookies = request.getCookies();
	    if (cookies != null){
	    	for(javax.servlet.http.Cookie ck : cookies){
	    		if (name.equals(ck.getName())){
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
    
    public boolean remove(){
		if(StringUtils.isEmpty(name))
	    	return false;
		
		javax.servlet.http.Cookie cookie = null;
	    
		javax.servlet.http.Cookie[] cookies = request.getCookies();
	    if (cookies != null){
	    	for(javax.servlet.http.Cookie ck : cookies){
	    		if (name.equals(ck.getName())){
	    			cookie = ck;
	    			break;
	    		}
	    	}
	    }
	
	    if(cookie == null){
	    	return false;
	    }
	    
	    cookie.setValue(null);
	    cookie.setMaxAge(0);
	    cookie.setPath("/");
	    response.addCookie(cookie);
	    return true;
	}
}
