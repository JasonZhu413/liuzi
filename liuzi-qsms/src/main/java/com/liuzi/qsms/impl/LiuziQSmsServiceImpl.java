package com.liuzi.qsms.impl;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.github.qcloudsms.SmsMultiSenderResult;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.SmsStatusPullCallbackResult;
import com.github.qcloudsms.SmsStatusPullReplyResult;
import com.github.qcloudsms.SmsVoicePromptSenderResult;
import com.github.qcloudsms.SmsVoiceVerifyCodeSenderResult;
import com.liuzi.qsms.LiuziQSmsService;
import com.liuzi.qsms.LiuziQSms;

@Service("liuziQSmsService")
public class LiuziQSmsServiceImpl implements LiuziQSmsService{

	/**
	 * 短信单发（普通）
	 * @param phone 电话号码
	 * @param msg 短信内容
	 * @return
	 * @throws Exception
	 */
	@Override
	public SmsSingleSenderResult send(String phone, String msg){
		return LiuziQSms.send(phone, msg);
	}
	
	/**
	 * 短信单发（指定模板）
	 * @param tempId 模板id（腾讯云）
	 * @param phone 电话号码
	 * @param params 内容
	 * @return
	 * @throws Exception
	 */
	public SmsSingleSenderResult send(int tempId, String phone, ArrayList<String> params){
		return LiuziQSms.send(tempId, phone, params);
	}
	
	/**
	 * 短信群发（普通）
	 * @param phones 电话号码
	 * @param msg 内容
	 * @return
	 * @throws Exception
	 */
	public SmsMultiSenderResult send(ArrayList<String> phones, String msg){
		return LiuziQSms.send(phones, msg);
	}
	
	/**
	 * 短信群发（指定模板）
	 * @param tempId 模板id（腾讯云）
	 * @param phones 电话号码
	 * @param params 内容
	 * @return
	 * @throws Exception
	 */
	public SmsMultiSenderResult send(int tempId, ArrayList<String> phones, ArrayList<String> params){
		return LiuziQSms.send(tempId, phones, params);
	}
	
	/**
	 * 短信语音验证码单发
	 * @param phone 电话号码
	 * @param msg 内容
	 * @return
	 * @throws Exception
	 */
	public SmsVoiceVerifyCodeSenderResult sendVoiceVerify(String phone, String msg){
		return LiuziQSms.sendVoiceVerify(phone, msg);
	}
	
	/**
	 * 语音通知单发
	 * @param phone 电话号码
	 * @param msg 内容
	 * @return
	 * @throws Exception
	 */
	public SmsVoicePromptSenderResult sendVoicePrompt(String phone, String msg){
		return LiuziQSms.sendVoicePrompt(phone, msg);
	}
	
	/**
	 * 拉取短信回执
	 * @return
	 * @throws Exception
	 */
	public SmsStatusPullCallbackResult callback(){
		return LiuziQSms.callback();
	}
	
	/**
	 * 拉取短信回复
	 * @return
	 * @throws Exception
	 */
	public SmsStatusPullReplyResult result(){
		return LiuziQSms.result();
	}
}
