package com.liuzi.util.encrypt;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Title:        MD5
 * 
 * @Description:  TODO
 * 
 * @author        ZhuShiyao
 * 
 * @Date          2017年3月30日 下午11:52:18
 * 
 * @version       1.0
 * 
 */
public class MD5 {
	static final char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', 
	    '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String crypt(String str){
		return crypt(str, "UTF8");
	}

	public static String crypt(String str, String charSet){
	    if ((str == null) || (str.length() == 0) || (charSet == null)) {
	      throw new IllegalArgumentException(
	        "String or charset to encript cannot be null or zero length");
	    }
	    return crypt(Charsets.getBytes(str, charSet));
	}

	public static String crypt(byte[] source){
	    String s = null;
	    try{
	    	MessageDigest md = MessageDigest.getInstance("MD5");
	    	md.update(source);
	    	byte[] tmp = md.digest();
	    	char[] str = new char[32];
	    	int k = 0;
	    	for (int i = 0; i < 16; ++i) {
	    		byte byte0 = tmp[i];
	    		str[(k++)] = hexDigits[(byte0 >>> 4 & 0xF)];
	    		str[(k++)] = hexDigits[(byte0 & 0xF)];
	    	}
	    	s = new String(str);
	    }catch (Exception e) {
	    	throw new IllegalArgumentException(e);
	    }
	    return s;
	}

	public static byte[] encode2bytes(String source){
	    byte[] result = null;
	    try {
	    	MessageDigest md = MessageDigest.getInstance("MD5");
	    	md.reset();
	    	md.update(source.getBytes("UTF-8"));
	    	result = md.digest();
	    } catch (NoSuchAlgorithmException e) {
	    	e.printStackTrace();
	    } catch (UnsupportedEncodingException e) {
	    	e.printStackTrace();
	    }
	    return result;
	}

	public static String encode2hex(String source){
	    byte[] data = encode2bytes(source);
	    StringBuffer hexString = new StringBuffer();
	    for (int i = 0; i < data.length; ++i) {
	    	String hex = Integer.toHexString(0xFF & data[i]);
	    	if (hex.length() == 1) {
	    		hexString.append('0');
	    	}
	    	hexString.append(hex);
	    }
	    return hexString.toString();
	}

	public static void main(String[] args) {
	    /*System.out.println(crypt("60000170230|WEB|WEB|813e7f5a-b134-410a-bfe8-958e533bee19|1408590570297"));
	    System.out.println(new Date(1408688761252L));
	    System.out.println(new Date(1408590570297L));*/
	}
}
