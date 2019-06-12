package com.liuzi.util.encrypt;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AESUtil {
	private static final String ALGORITHM = "AES";
	private static final String ALGORITHMSTR = "AES/CBC/PKCS5Padding";
	//AES-128-CBC加密 16位
	private static final String DEFAULT_KEY = "1234567890abcdef";
	//偏移量字符串必须是16位 当模式是CBC的时候必须设置偏移量
	private static final String DEFAULT_IV = "1234567890abcdef";
	
	private static final Charset CHARSET = Charset.forName("utf-8");

    public static String encrypt(String value){
    	return encrypt(value, DEFAULT_KEY, DEFAULT_IV);
    }
    
    public static String encrypt(String value, String key, String iv){
    	byte[] encrypt = encryptByte(value, key, iv);
    	return byteToHexString(encrypt);
    }
    
    public static byte[] encryptByte(String value){
    	return encryptByte(value, DEFAULT_KEY, DEFAULT_IV);
    }
    
    public static byte[] encryptByte(String value, String key, String iv){
    	byte[] cipherBytes = null;
    	try{
    		SecretKey secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            IvParameterSpec ivParameterSpec = getIv(iv);
            Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
            cipherBytes = cipher.doFinal(value.getBytes(CHARSET));
    	}catch(Exception e){
    		
    	}
        return cipherBytes;
    }
    
    public static String decrypt(String value){
    	return decrypt(value, DEFAULT_KEY, DEFAULT_IV);
    }
    
    public static String decrypt(String value, String key, String iv){
    	byte[] decrypt = decryptByte(value, key, iv);
    	try {
			return new String(decrypt, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    
    public static byte[] decryptByte(String value){
    	return decryptByte(value, DEFAULT_KEY, DEFAULT_IV);
    }

    public static byte[] decryptByte(String src, String key, String iv) {
    	byte[] plainBytes = null;
    	try{
    		SecretKey secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            IvParameterSpec ivParameterSpec = getIv(iv);
            Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            byte[] hexBytes = hexStringToBytes(src);
            plainBytes = cipher.doFinal(hexBytes);
    	}catch(Exception e){
    		
    	}
        return plainBytes;
    }
    
    public static byte[] generatorKey() {
        KeyGenerator keyGenerator = null;
		try {
			keyGenerator = KeyGenerator.getInstance(ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
        keyGenerator.init(256);//默认128，获得无政策权限后可为192或256
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

    public static IvParameterSpec getIv(String iv){
        IvParameterSpec ivParameterSpec = null;
		try {
			ivParameterSpec = new IvParameterSpec(iv.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        //log.info("偏移量：" + byteToHexString(ivParameterSpec.getIV()));
        return ivParameterSpec;
    }

    /**
     * 将byte转换为16进制字符串
     * @param src
     * @return
     */
    public static String byteToHexString(byte[] src) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xff;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                sb.append("0");
            }
            sb.append(hv);
        }
        return sb.toString();
    }

    /**
     * 将16进制字符串装换为byte数组
     * @param hexString
     * @return
     */
    public static byte[] hexStringToBytes(String hexString) {
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] b = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            b[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return b;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static void main(String[] args) {
    	String p = encrypt("15210050811");
        System.out.println(p);
        System.out.println(decrypt(p));
        
        String e = encrypt("dhgjdnameiqjwndmcjaksmgnejwkqlsjcnejgnamcjenskancjgnejqusnamckj@jguqmaiw");
        System.out.println(e);
        System.out.println(e.length());
        System.out.println(decrypt(e));
        
        String u = encrypt("zhushiyao");
        System.out.println(u);
        System.out.println(decrypt(u));
    }
}
