package com.liuzi.qsms;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.SmsStatusPullCallbackResult;
import com.github.qcloudsms.SmsStatusPullReplyResult;
import com.github.qcloudsms.SmsStatusPuller;
import com.github.qcloudsms.SmsVoicePromptSender;
import com.github.qcloudsms.SmsVoicePromptSenderResult;
import com.github.qcloudsms.SmsVoiceVerifyCodeSender;
import com.github.qcloudsms.SmsVoiceVerifyCodeSenderResult;


public class QSms{

	private Logger logger = LoggerFactory.getLogger(QSms.class);
	
	@Getter @Setter private int appId;
	@Getter @Setter private String appKey;
	
	/**
	 * 短信单发（普通）
	 * @param phone 电话号码
	 * @param msg 短信内容
	 * @return
	 * @throws Exception
	 */
	public SmsSingleSenderResult send(String phone, String msg){
		SmsSingleSender sender;
		SmsSingleSenderResult result = null;
		try {
			sender = new SmsSingleSender(appId, appKey);
			result = sender.send(0, "86", phone, msg, "", "");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("短信单发（普通）失败：" + e.getMessage());
		}
		logger.info("短信单发（普通）返回：" + result);
		return result;
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
		SmsSingleSender sender;
		SmsSingleSenderResult result = null;
		try {
			sender = new SmsSingleSender(appId, appKey);
			result = sender.sendWithParam("86", phone, tempId, params, "", "", "");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("短信单发（指定模板）失败：" + e.getMessage());
		}
		logger.info("短信单发（指定模板）返回：" + result);
		return result;
	}
	
	/**
	 * 短信群发（普通）
	 * @param phones 电话号码
	 * @param msg 内容
	 * @return
	 * @throws Exception
	 */
	public SmsMultiSenderResult send(ArrayList<String> phones, String msg){
		SmsMultiSender sender;
		SmsMultiSenderResult result = null;
		try {
			sender = new SmsMultiSender(appId, appKey);
			result = sender.send(0, "86", phones, msg, "", "");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("短信群发（普通）失败：" + e.getMessage());
		}
		logger.info("短信群发（普通）返回：" + result);
		return result;
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
		SmsMultiSender sender;
		SmsMultiSenderResult result = null;
		try {
			sender = new SmsMultiSender(appId, appKey);
			result = sender.sendWithParam("86", phones, tempId, params, "", "", "");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("短信群发（指定模板）失败：" + e.getMessage());
		}
		logger.info("短信群发（指定模板）返回：" + result);
		return result;
	}
	
	/**
	 * 短信语音验证码单发
	 * @param phone 电话号码
	 * @param msg 内容
	 * @return
	 * @throws Exception
	 */
	public SmsVoiceVerifyCodeSenderResult sendVoiceVerify(String phone, String msg){
		SmsVoiceVerifyCodeSender sender = new SmsVoiceVerifyCodeSender(appId, appKey);
		SmsVoiceVerifyCodeSenderResult result = null;
		try {
			result = sender.send("86", phone, msg, 2, "");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("短信语音验证码单发失败：" + e.getMessage());
		}
		logger.info("短信语音验证码单发返回：" + result);
		return result;
	}
	
	/**
	 * 语音通知单发
	 * @param phone 电话号码
	 * @param msg 内容
	 * @return
	 * @throws Exception
	 */
	public SmsVoicePromptSenderResult sendVoicePrompt(String phone, String msg){
		SmsVoicePromptSender sender = new SmsVoicePromptSender(appId, appKey);
		SmsVoicePromptSenderResult result = null;
		try {
			result = sender.send("86", phone, 2, 2, msg, "");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("语音通知单发失败：" + e.getMessage());
		}
		logger.info("语音通知单发返回：" + result);
		return result;
	}
	
	/**
	 * 拉取短信回执
	 * @return
	 * @throws Exception
	 */
	public SmsStatusPullCallbackResult callback(){
		SmsStatusPuller pullstatus = new SmsStatusPuller(appId, appKey);
		SmsStatusPullCallbackResult callback = null;
		try {
			callback = pullstatus.pullCallback(10);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("拉取短信回执失败：" + e.getMessage());
		}
		logger.info("拉取短信回执返回：" + callback);
		return callback;
	}
	
	/**
	 * 拉取短信回复
	 * @return
	 * @throws Exception
	 */
	public SmsStatusPullReplyResult result(){
		SmsStatusPuller pullstatus = new SmsStatusPuller(appId, appKey);
		SmsStatusPullReplyResult result = null;
		try {
			result = pullstatus.pullReply(10);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("拉取短信回复失败：" + e.getMessage());
		}
		
		logger.info("拉取短信回复返回：" + result);
		return result;
	}
	
	public void main(String[] args) {
		try {
			SmsSingleSender sender = new SmsSingleSender(appId, appKey);
			ArrayList<String> params = new ArrayList<String>();
			params.add("123456");
			params.add("5");
			
			SmsSingleSenderResult result = sender.sendWithParam("86", "15210050811", 102208, params, "", "", "");
		    System.out.print(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
