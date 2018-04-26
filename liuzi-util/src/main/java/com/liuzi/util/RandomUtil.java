package com.liuzi.util;

import org.apache.commons.lang.RandomStringUtils;

/**
 * @Title:        RandomUtil
 * 
 * @Description:  TODO
 * 
 * @author        ZhuShiyao
 * 
 * @Date          2017年3月30日 下午11:34:34
 * 
 * @version       1.0
 * 
 */
public class RandomUtil {
	private final static String DEFAULT_NUM = "0123456789";
	private final static String DEFAULT_ENG = "abcdefghijklmnopqrstuvwxyz";
	
	public static String random(int len){
	    return RandomStringUtils.random(len, DEFAULT_NUM);
	}
	
	public static String random(String str,int len){
	    return RandomStringUtils.random(len, str);
	}
	
	public static String random_eng(int len){
	    return RandomStringUtils.random(len, DEFAULT_ENG);
	}
	
	public static String random_eng(String str, int len){
	    return RandomStringUtils.random(len, str);
	}
	
	public static void main(String[] args) {
		System.out.println(random_eng(5));
	}
}
