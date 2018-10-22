package com.liuzi.easemob.boot.api.impl;

import com.liuzi.easemob.boot.api.AuthTokenAPI;
import com.liuzi.easemob.boot.EasemobConfig;



public class EasemobAuthToken extends EasemobConfig implements AuthTokenAPI{

	@Override
	public Object getAuthToken(){
		return  getAccessToken();
	}
}
