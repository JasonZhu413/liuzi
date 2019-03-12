package com.liuzi.util.encrypt;


import java.io.UnsupportedEncodingException;

/**
 * @Title:        Charsets
 * 
 * @Description:  TODO
 * 
 * @author        ZhuShiyao
 * 
 * @Date          2017年3月30日 下午11:52:52
 * 
 * @version       1.0
 * 
 */
public enum Charsets {
	GBK, UTF8, ISO8859;

	public String encoding;

	public String toString(byte[] bytes){
	    return toString(bytes, this.encoding);
	}

	public byte[] getBytes(String s) {
	    return getBytes(s, this.encoding);
	}

	public static byte[] getBytes(String s, String encoding) {
	    try {
	      return s.getBytes(encoding); } catch (UnsupportedEncodingException e) {
	    }
	    return s.getBytes();
	}

	public static String toString(byte[] bytes, String encoding){
	    try {
	      return new String(bytes, encoding); } catch (UnsupportedEncodingException e) {
	    }
	    return new String(bytes);
	}
}
