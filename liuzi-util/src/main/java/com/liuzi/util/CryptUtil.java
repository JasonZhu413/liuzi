package com.liuzi.util;



import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Title:        CryptUtil
 * 
 * @Description:  TODO
 * 
 * @author        ZhuShiyao
 * 
 * @Date          2017年3月30日 下午11:21:36
 * 
 * @version       1.0
 * 
 */
public class CryptUtil {
	private static Logger logger = LoggerFactory.getLogger(CryptUtil.class);

	private static final byte[] DES_KEY = { 21, 1, -110, 82, -32, -85, -128, -65 };

	public static Key toKey(byte[] key)throws InvalidKeySpecException, InvalidKeyException,
		NoSuchAlgorithmException{
		
	    DESKeySpec deskey = new DESKeySpec(key);

	    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");

	    return keyFactory.generateSecret(deskey);
	}

	public static String encrypt(byte[] data) {
	    return encrypt(data, DES_KEY);
	}
	
	public static String encrypt(String data) {
		byte[] str = data.getBytes();
	    return encrypt(str, DES_KEY);
	}

	public static String encrypt(byte[] data, byte[] key){
	    String encryptedData = null;
	    try{
	    	SecureRandom sr = new SecureRandom();

	    	Cipher cipher = Cipher.getInstance("DES");
	    	cipher.init(1, toKey(key), sr);

	    	encryptedData = new String(Base64.encodeBase64(cipher.doFinal(data)), "UTF-8");
	    	
	    }catch (Exception e) {
	    	logger.error("", e);
	    	return null;
	    }
	    return encryptedData;
	}

	public static byte[] decrypt(String data){
	    return decrypt(data, DES_KEY);
	}

	public static byte[] decrypt(String data, byte[] key) {
	    byte[] decryptedData = null;
	    try{
	    	SecureRandom sr = new SecureRandom();

	    	Cipher cipher = Cipher.getInstance("DES");
	    	cipher.init(2, toKey(key), sr);

	    	decryptedData = cipher.doFinal(Base64.decodeBase64(data.getBytes("UTF-8")));
	    	
	    }catch (Exception e) {
	    	logger.error("", e);

	    	return null;
	    }
	    return decryptedData;
	}
	
	public static String decryp(String data) {
		return new String(decrypt(data, DES_KEY));
	}

	public static final String md5(String string){
	    char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	    try {
	    	byte[] strTemp = string.getBytes();
	    	MessageDigest mdTemp = MessageDigest.getInstance("MD5");
	    	mdTemp.update(strTemp);
	    	byte[] md = mdTemp.digest();
	    	int j = md.length;
	    	char[] str = new char[j * 2];
	    	int k = 0;
	    	for (int i = 0; i < j; ++i) {
	    		byte byte0 = md[i];
	    		str[(k++)] = hexDigits[(byte0 >>> 4 & 0xF)];
	    		str[(k++)] = hexDigits[(byte0 & 0xF)];
	    	}
	    	return new String(str);
	    } catch (Exception e) {
	    	logger.error("", e);
	    }
	    return null;
	}

	public static void main(String[] args){
	    String data = "123456789";
	    data = encrypt(data.getBytes());
	    System.out.println(data);

	    //data = new String(decrypt(data));
	    data = decryp(data);
	    System.out.println(data);
	    
	    System.out.println(md5(data));
	}
}
