package com.liuzi.util.common;

import java.io.UnsupportedEncodingException;

public class ElUtil {
	
	public static String formatPhone(String phone){
		return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

	public static String formatIdCard(String idCard){
		return idCard.replaceAll("(\\d{5})\\d{10}(\\d{3})", "$1****$2");
    }
	
	public static String formatEmail(String email){
		return email.replaceAll("(\\w{2})(\\w+)(@\\w+)", "$1****$3");
    }
	
	public static String formatText(String text, Integer count){
		if(count == null || count == 0){
			return text;
		}
		
		if(text.length() <= count){
			return text;
		}
		
		return text.substring(0, count) + "...";
    }
	
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		String text = "ha{是【哈哈";
		byte[] b3 = text.getBytes("GBK");
		System.out.println(b3.length/2 + (b3.length%2==0?0:0.5));
	}
}
