package com.liuzi.jPush.service;

import com.liuzi.jPush.PushModel;

public interface JPushService {

	/**
	 * 推送android和ios
	 * @param model
	 */
	public void push(PushModel model);
	
	/**
	 * 仅推送android
	 * @param model
	 */
	public void pushAndroid(PushModel model);
	
	/**
	 * 仅提送ios
	 * @param model
	 */
	public void pushIOS(PushModel model);
}
