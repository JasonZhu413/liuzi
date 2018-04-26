package com.liuzi.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @Title:        IPUtil
 * 
 * @Description:  TODO
 * 
 * @author        ZhuShiyao
 * 
 * @Date          2017年3月24日 下午4:14:27
 * 
 * @version       1.0
 * 
 */
public class IPUtil {
	public static String getIP(HttpServletRequest request){
	    String ip = request.getHeader("x-forwarded-for");
	    if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
	    	ip = request.getHeader("Proxy-Client-IP");
	    }
	    if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
	    	ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
	    	ip = request.getRemoteAddr();
	    }
	    return ip;
	}

	public static String getUserAgent(HttpServletRequest request) {
		String userAgent = request.getHeader("User-Agent");

		return userAgent;
	}

	public static Integer getUserSourceId(String userAgent){
		Integer sourceId = Integer.valueOf(0);
		if (userAgent == null) {
		  return sourceId;
		}
		boolean isFromMobile = false;
		String ua = userAgent.toLowerCase();
		String iphone = "iphone os".toLowerCase();
		String android = "android".toLowerCase();
		String ipod = "ipod".toLowerCase();
		String ipad = "ipad".toLowerCase();
		String wp = "windows phone".toLowerCase();
		String wm = "windows mobile".toLowerCase();
		//String wc = "windows ce".toLowerCase();
		String macPc = "Macintosh".toLowerCase();
		String winPc = "windows nt".toLowerCase();
		isFromMobile = CheckMobile.check(userAgent);
	    if (isFromMobile) {
	    	if (ua.indexOf(iphone) > -1)
	    		sourceId = Integer.valueOf(1);
	    	else if (ua.indexOf(android) > -1)
	    		sourceId = Integer.valueOf(2);
	    	else if (ua.indexOf(ipad) > -1)
	    		sourceId = Integer.valueOf(3);
	    	else if (ua.indexOf(ipod) > -1)
	    		sourceId = Integer.valueOf(4);
	    	else if (ua.indexOf(wp) > -1)
	    		sourceId = Integer.valueOf(5);
	    	else if (ua.indexOf(wm) > -1)
	    		sourceId = Integer.valueOf(6);
	    	else {
	    		sourceId = Integer.valueOf(0);
	    	}
	    }else if (ua.indexOf(macPc) > -1)
	    	sourceId = Integer.valueOf(11);
	    else if (ua.indexOf(winPc) > -1)
	    	sourceId = Integer.valueOf(12);
	    else {
	    	sourceId = Integer.valueOf(0);
	    }

	    return sourceId;
	}

	public static void main(String[] args) {
		String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.152 Safari/537.36";
		Integer sourceId = getUserSourceId(userAgent);
		System.out.println(sourceId);
	}
}
