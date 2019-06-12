package com.liuzi.util.wechat;



public class Wechat extends WechatConfig{
	
	
	
	public String getCode(){
		StringBuffer sbf = new StringBuffer();
		sbf.append(QR_CONNECT);
		sbf.append("?appid=" + appId);
		sbf.append("&redirect_uri=" + redirectUri);
		sbf.append("&response_type=" + responseType);
		sbf.append("&scope=" + scope);
		sbf.append("&state=" + state);
		return sbf.toString();
	}
	
	
	public static void main(String[] args) {
		Wechat wechat = new Wechat();
		wechat.setAppId("wx898059b125886d5f");
		wechat.setAppSecret("848a754a73ee6d0924c159d6c8d38680");
		wechat.setRedirectUri("http://127.0.0.1:8080");
		wechat.setState("state");
		System.out.println(wechat.getCode());
	}

}
