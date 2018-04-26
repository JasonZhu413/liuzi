package com.liuzi.util;

public class LiuziElUtil {
	
	public static String formatPhone(String phone){
		return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

	public static String formatIdCard(String idCard){
		return idCard.replaceAll("(\\d{5})\\d{10}(\\d{3})", "$1****$2");
    }
	
	public static String formatEmail(String email){
		return email.replaceAll("(\\w{2})(\\w+)(@\\w+)", "$1****$3");
    }
	
	public static void main(String[] args) {
		System.out.println(formatEmail("554157554@123.com.cn"));
	}
}
