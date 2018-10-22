package com.liuzi.easemob.boot.api.impl;

import com.liuzi.easemob.boot.api.AuthTokenAPI;
import com.liuzi.easemob.boot.EasemobConfig;



public class EasemobAuthToken implements AuthTokenAPI{

	@Override
	public Object getAuthToken(){
		return EasemobConfig.getAccessToken();
	}
}
