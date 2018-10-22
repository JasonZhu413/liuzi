package com.liuzi.qsms.boot.service.impl;

import java.util.ArrayList;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

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
import com.liuzi.qsms.boot.service.QSmsService;

@Slf4j
@Service("qSmsService")
public class QSmsServiceImpl implements QSmsService{
	
	@Resource
	private SmsSingleSender smsSingleSender;
	@Resource
	private SmsMultiSender smsMultiSender;
	@Resource
	private SmsVoiceVerifyCodeSender smsVoiceVerifyCodeSender;
	@Resource
	private SmsVoicePromptSender smsVoicePromptSender;
	@Resource
	private SmsStatusPuller smsStatusPuller;
	
	
	/**
	 * 短信单发（普通）
	 * @param phone 电话号码
	 * @param msg 短信内容
	 * @return
	 * @throws Exception
	 */
	public SmsSingleSenderResult send(String phone, String msg){
		SmsSingleSenderResult result = null;
		try {
			result = smsSingleSender.send(0, "86", phone, msg, "", "");
		} catch (Exception e) {
			e.printStackTrace();
			log.info("短信单发（普通）失败：" + e.getMessage());
		}
		log.info("短信单发（普通）返回：" + result);
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
		SmsSingleSenderResult result = null;
		try {
			result = smsSingleSender.sendWithParam("86", phone, tempId, params, "", "", "");
		} catch (Exception e) {
			e.printStackTrace();
			log.info("短信单发（指定模板）失败：" + e.getMessage());
		}
		log.info("短信单发（指定模板）返回：" + result);
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
		SmsMultiSenderResult result = null;
		try {
			result = smsMultiSender.send(0, "86", phones, msg, "", "");
		} catch (Exception e) {
			e.printStackTrace();
			log.info("短信群发（普通）失败：" + e.getMessage());
		}
		log.info("短信群发（普通）返回：" + result);
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
		SmsMultiSenderResult result = null;
		try {
			result = smsMultiSender.sendWithParam("86", phones, tempId, params, "", "", "");
		} catch (Exception e) {
			e.printStackTrace();
			log.info("短信群发（指定模板）失败：" + e.getMessage());
		}
		log.info("短信群发（指定模板）返回：" + result);
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
		SmsVoiceVerifyCodeSenderResult result = null;
		try {
			result = smsVoiceVerifyCodeSender.send("86", phone, msg, 2, "");
		} catch (Exception e) {
			e.printStackTrace();
			log.info("短信语音验证码单发失败：" + e.getMessage());
		}
		log.info("短信语音验证码单发返回：" + result);
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
		SmsVoicePromptSenderResult result = null;
		try {
			result = smsVoicePromptSender.send("86", phone, 2, 2, msg, "");
		} catch (Exception e) {
			e.printStackTrace();
			log.info("语音通知单发失败：" + e.getMessage());
		}
		log.info("语音通知单发返回：" + result);
		return result;
	}
	
	/**
	 * 拉取短信回执
	 * @return
	 * @throws Exception
	 */
	public SmsStatusPullCallbackResult callback(){
		SmsStatusPullCallbackResult callback = null;
		try {
			callback = smsStatusPuller.pullCallback(10);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("拉取短信回执失败：" + e.getMessage());
		}
		log.info("拉取短信回执返回：" + callback);
		return callback;
	}
	
	/**
	 * 拉取短信回复
	 * @return
	 * @throws Exception
	 */
	public SmsStatusPullReplyResult result(){
		SmsStatusPullReplyResult result = null;
		try {
			result = smsStatusPuller.pullReply(10);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("拉取短信回复失败：" + e.getMessage());
		}
		
		log.info("拉取短信回复返回：" + result);
		return result;
	}
	
	public static void main(String[] args) {
		try {
			/*SmsSingleSender sender = new SmsSingleSender(app_id, app_key);
			ArrayList<String> params = new ArrayList<String>();
			params.add("123456");
			params.add("5");
			SmsSingleSenderResult result = sender.sendWithParam("86", 
					"15210050811", 102208, params, "", "", "");
		    System.out.print(result);*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
