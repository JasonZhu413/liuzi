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
	public static String random(int len){
	    String str = "0123456789";
	    return RandomStringUtils.random(len, str);
	}
	
	public static void main(String[] args) {
		System.out.println(RandomStringUtils.random(5,"0123456789"));
	}
}
