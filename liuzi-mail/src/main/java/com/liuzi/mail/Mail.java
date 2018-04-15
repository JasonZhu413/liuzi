package com.liuzi.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;

public class Mail extends MailConfig{
	
	private static Logger logger = LoggerFactory.getLogger(Mail.class);
	
	public Mail(String confFile) {
		super(confFile);
	}
	
	static{
		if (properties == null){
		    synchronized(Mail.class) {
		    	if (properties == null){
		    		init();
		    	}
		    }
		}
	}
	
	/**
	 * 发送邮件
	 *
	 * @param subject        邮件主题
	 * @param mailBody       邮件内容
	 * @param receiveUser    收件人地址
	 */
	public static void send(String subject, String mailBody, 
			String receiveUser){
		send(subject, mailBody, null, receiveUser, false);
	}
	
	/**
	 * 发送邮件
	 *
	 * @param subject        邮件主题
	 * @param mailBody       邮件内容
	 * @param receiveUser    收件人地址
	 * @param isHtmlFormat 是否html格式
	 */
	public static void send(String subject, String mailBody, 
			String receiveUser, Boolean isHtmlFormat){
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
	public static void send(String subject, String mailBody, String senderNickName, 
			String receiveUser){
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
	public static void send(String subject, String mailBody, String senderNickName, 
			String receiveUser, Boolean isHtmlFormat){
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
	public static void send(String subject, String mailBody, String senderNickName, 
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
	public static void send(String subject, String mailBody, String senderNickName, 
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
	public static void send(String subject, String mailBody, String senderNickName,
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
	public static void send(String subject, String mailBody, String senderNickName,
	                      String receiveUser, String ccReceiveUser, String bccReceiveUser, 
	                      Boolean isHtmlFormat) {
		if(!is_init()){
			logger.warn("邮件发送失败，未初始化配置。。。");
			return;
		}
		
		if (StringUtils.isEmpty(subject)) {
			logger.warn("邮件发送失败，缺失主题，取消发送。。。");
			return;
        }
		
		if (StringUtils.isEmpty(mailBody)) {
			logger.warn("邮件发送失败，缺失内容，取消发送。。。");
			return;
        }
		
		if (StringUtils.isEmpty(receiveUser)) {
			logger.warn("邮件发送失败，缺失收件人地址，取消发送。。。");
			return;
        }
		
	    try {
	        // 发件人
	        InternetAddress from = null;
	        if (StringUtils.isEmpty(senderNickName)) {
	            from = new InternetAddress(senderUserName);
	        } else {
	            from = new InternetAddress(MimeUtility.encodeWord(senderNickName) + " <" + senderUserName + ">");
	        }
	        message.setFrom(from);

	        // 收件人
	        InternetAddress to = new InternetAddress(receiveUser);
	        message.setRecipient(Message.RecipientType.TO, to);

	        //抄送人
	        if (!StringUtils.isEmpty(ccReceiveUser)) {
	            InternetAddress cc = new InternetAddress(ccReceiveUser);
	            message.setRecipient(Message.RecipientType.CC, cc);
	        }

	        //密送人
	        if (!StringUtils.isEmpty(bccReceiveUser)) {
	            InternetAddress bcc = new InternetAddress(bccReceiveUser);
	            message.setRecipient(Message.RecipientType.BCC, bcc);
	        }

	        message.setSubject(subject);
	        String content = mailBody.toString();

	        if (isHtmlFormat) {
	            message.setContent(content, "text/html;charset=UTF-8");
	        } else {
	            message.setContent(content, "text/plain;charset=UTF-8");
	        }
	        message.saveChanges();
	        transport = session.getTransport("smtp");
	        transport.connect(smtpHost, smtpPort, senderUserName, senderPassword);
	        transport.sendMessage(message, message.getAllRecipients());

	        logger.debug(senderUserName + " 向 " + receiveUser + " 发送邮件成功！");

	    } catch (Exception e) {
	        e.printStackTrace();
	        logger.error("sendEmail失败！", e);
	    } finally {
	        if (transport != null) {
	            try {
	                transport.close();
	            } catch (MessagingException e) {
	                e.printStackTrace();
	                logger.error("sendEmail->transport关闭失败！", e);
	            }
	        }
	    }
	}
	
	private static boolean is_init(){
		if(message == null) return false;
		
		return true;
	}
}
