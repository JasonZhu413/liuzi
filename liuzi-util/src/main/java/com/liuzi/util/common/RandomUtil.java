package com.liuzi.util.common;

import java.util.Random;

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
	private final static String DEFAULT_ENG_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private final static String DEFAULT_ALL = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	
	private static Random random;
	
	private static Random random(){
		if(random == null){
			random = new Random();
		}
		return random;
	}
	
	public static String all(int len){
	    return RandomStringUtils.random(len, DEFAULT_ALL);
	}
	
	public static String num(int len){
	    return RandomStringUtils.random(len, DEFAULT_NUM);
	}
	
	public static String letter(int len){
	    return RandomStringUtils.random(len, DEFAULT_ENG);
	}
	
	public static String letterUp(int len){
	    return RandomStringUtils.random(len, DEFAULT_ENG_UPPER);
	}
	
	public static String custom(String str, int len){
	    return RandomStringUtils.random(len, str);
	}
	
	public static int nextInt(int max){
	    return random().nextInt(max);
	}
	
	public static int nextInt(int min, int max){
		return random().nextInt(max - min) + min;
	}
	
	public static void main(String[] args) {
		
	}
}
