package com.liuzi.jPush.boot.service;

import com.liuzi.jPush.boot.JPushModel;

public interface JPushService {

	/**
	 * 推送android和ios
	 * @param model
	 */
	public void push(JPushModel model);
	
	/**
	 * 仅推送android
	 * @param model
	 */
	public void pushAndroid(JPushModel model);
	
	/**
	 * 仅提送ios
	 * @param model
	 */
	public void pushIOS(JPushModel model);
}
