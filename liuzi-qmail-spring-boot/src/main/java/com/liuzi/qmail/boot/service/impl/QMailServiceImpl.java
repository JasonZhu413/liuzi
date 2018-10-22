package com.liuzi.qmail.boot.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.velocity.VelocityContext;
import org.springframework.stereotype.Service;

import com.liuzi.qmail.boot.QMail;
import com.liuzi.qmail.boot.service.QMailService;


@Service("qMailService")
public class QMailServiceImpl implements QMailService{
	
	@Resource
	private QMail qMail;

	/**
	 * 发送邮件
	 *
	 * @param subject        邮件主题
	 * @param mailBody       邮件内容
	 * @param receiveUser    收件人地址
	 */
	@Override
	public void send(String subject, String mailBody, String receiveUser){
		send(subject, mailBody, receiveUser, false);
	}
	
	/**
	 * 发送邮件
	 *
	 * @param subject        邮件主题
	 * @param mailBody       邮件内容
	 * @param receiveUser    收件人地址
	 * @param isHtmlFormat 是否html格式
	 */
	@Override
	public void send(String subject, String mailBody, String receiveUser, Boolean isHtmlFormat){
		send(subject, mailBody, null, receiveUser, isHtmlFormat);
	}
	
	/**
	 * 发送邮件
	 *
	 * @param subject        邮件主题
	 * @param mailBody       邮件内容
	 * @param senderNickName 发件人NickName
	 * @param receiveUser    收件人地址
	 * 
	 */
	@Override
	public void send(String subject, String mailBody, String senderNickName, String receiveUser){
		send(subject, mailBody, senderNickName, receiveUser, null, false);
	}
	
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
	@Override
	public void send(String subject, String mailBody, String senderNickName, String receiveUser, Boolean isHtmlFormat){
		send(subject, mailBody, senderNickName, receiveUser, null, isHtmlFormat);
	}
	
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
	@Override
	public void send(String subject, String mailBody, String senderNickName, 
			String receiveUser, String ccReceiveUser){
		send(subject, mailBody, senderNickName, receiveUser, ccReceiveUser, null, false);
	}
	
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
	@Override
	public void send(String subject, String mailBody, String senderNickName, 
			String receiveUser, String ccReceiveUser, Boolean isHtmlFormat){
		send(subject, mailBody, senderNickName, receiveUser, ccReceiveUser, null, isHtmlFormat);
	}
	
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
	@Override
	public void send(String subject, String mailBody, String senderNickName,
            String receiveUser, String ccReceiveUser, String bccReceiveUser){
		send(subject, mailBody, senderNickName, receiveUser, ccReceiveUser, bccReceiveUser, false);
	}

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
	@Override
	public void send(String subject, String mailBody, String senderNickName, String receiveUser, 
			String ccReceiveUser, String bccReceiveUser, Boolean isHtmlFormat) {
		qMail.send(subject, mailBody, senderNickName, receiveUser, ccReceiveUser, bccReceiveUser, isHtmlFormat);
	}
	

	@Override
	public void sendByTemp(String subject, String receiveUser, String path) {
		sendByTemp(subject, receiveUser, path, new HashMap<String, Object>());
	}

	@Override
	public void sendByTemp(String subject, String receiveUser, String path,
			Map<String, Object> map) {
		qMail.sendByTemp(subject, receiveUser, path, map);
	}

	@Override
	public void sendByTemp(String subject, String receiveUser, String path, 
			VelocityContext vc){
		qMail.sendByTemp(subject, receiveUser, path, vc);
	}
}
