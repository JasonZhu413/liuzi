package com.liuzi.util.token;


import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;

import lombok.Data;


@Data
public class TokenConfig{
	
	/**
     * 初始化Header
     * {
     *     "alg":"HS256",
     *     "type":"JWT"
     * }
     */
	protected static final JWSHeader HEADER = new JWSHeader(JWSAlgorithm.HS256,
            JOSEObjectType.JWT, null, null, null, null, null, null, null,
            null, null, null, null);
    /**
     * 创建时间属性
     */
	protected static final String TOKEN_CREATE_TIME = "TOKEN_CREATE_TIME";
    /**
     * 过期时间属性
     */
    protected static final String TOKEN_EXP_TIME = "TOKEN_EXP_TIME";
    /**
     * 日志打印：时间格式
     */
    protected static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * pc超时时间（分钟）
     * 默认30分钟
     */
    protected static final double TOKEN_PC_VALID_TIME = 30D;
    /**
     * app超时时间（分钟）
     * 默认30天，30 * 24 * 60
     */
    protected static final double TOKEN_APP_VALID_TIME = (double)(30 * 24 * 60);
	/**
	 * 秘钥
	 * 默认123456
	 */
    protected static final String TOKEN_SECRET = "123456";
	/**
	 * 是否为开发环境
	 * 默认true
	 */
    protected static final boolean TOKEN_DEBUG = true;
}
