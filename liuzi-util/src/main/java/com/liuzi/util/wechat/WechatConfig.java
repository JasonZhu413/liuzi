package com.liuzi.util.wechat;

import lombok.Data;


@Data
public class WechatConfig {
	
	protected static final String QR_CONNECT = "https://open.weixin.qq.com/connect/qrconnect";
	private static final String RESPONSE_TYPE = "code";
	private static final String SCOPE = "snsapi_login";
	private static final String STATE = System.currentTimeMillis() + "";
	
	protected String appId;
	
	protected String appSecret;
	
	protected String redirectUri;
	
	protected String responseType = RESPONSE_TYPE;
	
	protected String scope = SCOPE;
	
	protected String state = STATE;
	
}
