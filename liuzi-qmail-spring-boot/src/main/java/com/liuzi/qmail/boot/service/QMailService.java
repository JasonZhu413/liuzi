package com.liuzi.qmail.boot.service;

import java.util.Map;

import org.apache.velocity.VelocityContext;

public interface QMailService {
	
	/**
	 * 发送邮件
	 *
	 * @param subject        邮件主题
	 * @param mailBody       邮件内容
	 * @param receiveUser    收件人地址
	 */
	public void send(String subject, String mailBody, String receiveUser);
	
	/**
	 * 发送邮件
	 *
	 * @param subject        邮件主题
	 * @param mailBody       邮件内容
	 * @param receiveUser    收件人地址
	 * @param isHtmlFormat 是否html格式
	 */
	public void send(String subject, String mailBody, 
			String receiveUser, Boolean isHtmlFormat);
	
	/**
	 * 发送邮件
	 *
	 * @param subject        邮件主题
	 * @param mailBody       邮件内容
	 * @param senderNickName 发件人NickName
	 * @param receiveUser    收件人地址
	 * 
	 */
	public void send(String subject, String mailBody, String senderNickName, 
			String receiveUser);
	
	/**
	 * 发送邮件
	 *
	 * @param subject        邮件主题
	 * @param mailBody       邮件内容
	 * @param senderNickName 发件人NickName
	 * @param receiveUser    收件人地址
	 * @param isHtmlFormat 是否html格式
	 * 
	 */
	public void send(String subject, String mailBody, String senderNickName, 
			String receiveUser, Boolean isHtmlFormat);
	
	/**
	 * 发送邮件
	 *
	 * @param subject        邮件主题
	 * @param mailBody       邮件内容
	 * @param senderNickName 发件人NickName
	 * @param receiveUser    收件人地址
	 * @param ccReceiveUser  抄送地址
	 * 
	 */
	public void send(String subject, String mailBody, String senderNickName, 
			String receiveUser, String ccReceiveUser);
	
	/**
	 * 发送邮件
	 *
	 * @param subject        邮件主题
	 * @param mailBody       邮件内容
	 * @param senderNickName 发件人NickName
	 * @param receiveUser    收件人地址
	 * @param ccReceiveUser  抄送地址
	 * @param isHtmlFormat 是否html格式
	 * 
	 */
	public void send(String subject, String mailBody, String senderNickName, 
			String receiveUser, String ccReceiveUser, Boolean isHtmlFormat);
	
	/**
	 * 发送邮件
	 *
	 * @param subject        邮件主题
	 * @param mailBody       邮件内容
	 * @param senderNickName 发件人NickName
	 * @param receiveUser    收件人地址
	 * @param ccReceiveUser  抄送地址
	 * @param bccReceiveUser 密送地址
	 */
	public void send(String subject, String mailBody, String senderNickName,
            String receiveUser, String ccReceiveUser, String bccReceiveUser);

	/**
	 * 发送邮件
	 *
	 * @param subject        邮件主题
	 * @param mailBody       邮件内容
	 * @param senderNickName 发件人NickName
	 * @param receiveUser    收件人地址
	 * @param ccReceiveUser  抄送地址
	 * @param bccReceiveUser 密送地址
	 * @param isHtmlFormat 是否html格式
	 */
	public void send(String subject, String mailBody, String senderNickName, String receiveUser, 
			String ccReceiveUser, String bccReceiveUser, Boolean isHtmlFormat);
	
	/**
	 * 利用模板发送邮件
	 * @param subject 主题
	 * @param receiveUser 收件人地址
	 * @param path 模板文件目录(默认classpath)
	 */
	public void sendByTemp(String subject, String receiveUser, String path);

	/**
	 * 利用模板发送邮件
	 * @param subject 主题
	 * @param receiveUser 收件人地址
	 * @param path 模板文件目录(默认classpath)
	 * @param map 模板内参数
	 */
	public void sendByTemp(String subject, String receiveUser, String path,
			Map<String, Object> map);
	
	/**
	 * 利用模板发送邮件
	 * @param subject 主题
	 * @param receiveUser 收件人地址
	 * @param path 模板文件目录(默认classpath)
	 * @param vc 板内参数
	 */
	public void sendByTemp(String subject, String receiveUser, String path, 
			VelocityContext vc);
}
