package com.liuzi.easemob.api.impl;

import com.liuzi.easemob.api.AuthTokenAPI;
import com.liuzi.easemob.EasemobConfig;



public class EasemobAuthToken implements AuthTokenAPI{

	@Override
	public Object getAuthToken(){
		return EasemobConfig.getAccessToken();
	}
}
