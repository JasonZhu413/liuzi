package com.liuzi.util;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AESUtil {
	
	private static Logger logger = LoggerFactory.getLogger(AESUtil.class);
	
	private static final String ALGORITHMSTR = "AES/CBC/PKCS5Padding";
	
	private static final String DEFAULT_KEY = "1234567890abcdef";//AES-128-CBC加密 16位
	private static final String DEFAULT_IV = "1234567890abcdef";
	
	/**
     * 使用默认的key和iv加密
     * @param data
     * @return
     * @throws Exception
     */
    public static String encrypt(String data) {
        return encrypt(data, DEFAULT_KEY, DEFAULT_IV);
    }
    
    /**
     * 加密方法
     * @param data  要加密的数据
     * @param key 加密key
     * @param iv 加密iv
     * @return 加密的结果
     * @throws Exception
     */
    public static String encrypt(String data, String key, String iv) {
        try {

            Cipher cipher = Cipher.getInstance(ALGORITHMSTR);//"算法/模式/补码方式"
            int blockSize = cipher.getBlockSize();

            byte[] dataBytes = data.getBytes();
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }

            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);

            return new Base64().encodeToString(encrypted);

        } catch (Exception e) {
        	logger.error("加密失败，错误：" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用默认的key和iv解密
     * @param data
     * @return
     * @throws Exception
     */
    public static String decrypt(String data) {
        return decrypt(data, DEFAULT_KEY, DEFAULT_IV);
    }
	
    /**
     * 解密方法
     * @param data 要解密的数据
     * @param key  解密key
     * @param iv 解密iv
     * @return 解密的结果
     * @throws Exception
     */
    public static String decrypt(String data, String key, String iv) {
        try {
            byte[] encrypted1 = new Base64().decode(data);

            Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);
            return originalString;
        } catch (Exception e) {
        	logger.error("解密失败，错误：" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    
	
	public static void main(String[] args) throws Exception {

		String test = "123456";

        String data = null;
        String key = "1234567890hahaha";
        String iv = "1234567890hahaha";

        data = encrypt(test, key, iv);

        System.out.println(data);
        System.out.println(decrypt(data, key, iv));
	}
}
