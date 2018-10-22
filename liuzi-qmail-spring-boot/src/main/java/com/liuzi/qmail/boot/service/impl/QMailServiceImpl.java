package com.liuzi.qmail.boot.service.impl;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import lombok.extern.slf4j.Slf4j;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.liuzi.qmail.boot.QMailConfig;
import com.liuzi.qmail.boot.service.QMailService;

@Slf4j
@Service("qMailService")
@Configuration
public class QMailServiceImpl implements QMailService{
	
	@Value("${mail.smtp.host}")
	public String smtpHost;
	@Value("${mail.smtp.port}")
	public int smtpPort = 25;
	@Value("${mail.smtp.username}")
	public String senderUserName;
	@Value("${mail.smtp.password}")
	public String senderPassword;

	@Resource
	private MimeMessage mimeMessage;
	@Resource
	private VelocityEngine velocityEngine;
	
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
	public void send(String subject, String mailBody, String senderNickName,
          String receiveUser, String ccReceiveUser, String bccReceiveUser, 
          Boolean isHtmlFormat) {
		log.info("------- 正在发送邮件...... ----------");
		
		if (StringUtils.isEmpty(subject)) {
			log.warn("邮件发送失败，缺失主题，取消发送。。。");
			return;
        }
		
		if (StringUtils.isEmpty(mailBody)) {
			log.warn("邮件发送失败，缺失内容，取消发送。。。");
			return;
        }
		
		if (StringUtils.isEmpty(receiveUser)) {
			log.warn("邮件发送失败，缺失收件人地址，取消发送。。。");
			return;
        }
		
		Transport transport = null;
	    try {
	        // 发件人
	        InternetAddress from = null;
	        if (StringUtils.isEmpty(senderNickName)) {
	            from = new InternetAddress(senderUserName);
	        } else {
	            from = new InternetAddress(MimeUtility.encodeWord(senderNickName) + " <" + 
	            		senderUserName + ">");
	        }
	        mimeMessage.setFrom(from);
	        
	        // 收件人
	        InternetAddress to = new InternetAddress(receiveUser);
	        mimeMessage.setRecipient(Message.RecipientType.TO, to);

	        //抄送人
	        if (!StringUtils.isEmpty(ccReceiveUser)) {
	            InternetAddress cc = new InternetAddress(ccReceiveUser);
	            mimeMessage.setRecipient(Message.RecipientType.CC, cc);
	        }

	        //密送人
	        if (!StringUtils.isEmpty(bccReceiveUser)) {
	            InternetAddress bcc = new InternetAddress(bccReceiveUser);
	            mimeMessage.setRecipient(Message.RecipientType.BCC, bcc);
	        }

	        mimeMessage.setSubject(subject);
	        String content = mailBody.toString();

	        if (isHtmlFormat) {
	        	mimeMessage.setContent(content, "text/html;charset=UTF-8");
	        } else {
	        	mimeMessage.setContent(content, "text/plain;charset=UTF-8");
	        }
	        mimeMessage.saveChanges();
	        //transport = QMailConfig.session.getTransport("smtp");
	        transport = QMailConfig.session.getTransport();
	        transport.connect(smtpHost, smtpPort, senderUserName, senderPassword);
	        transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
	        
	        log.info(senderUserName + " 向 " + receiveUser + " 发送邮件成功！");

	    } catch (Exception e) {
	        e.printStackTrace();
	        log.error("sendEmail失败！", e);
	    } finally {
	        if (transport != null) {
	            try {
	                transport.close();
	            } catch (MessagingException e) {
	                e.printStackTrace();
	                log.error("sendEmail->transport关闭失败！", e);
	            }
	        }
	    }
	}

	@Override
	public void sendByTemp(String subject, String receiveUser, String path) {
		sendByTemp(subject, receiveUser, path, new HashMap<String, Object>());
	}

	@Override
	public void sendByTemp(String subject, String receiveUser, String path,
			Map<String, Object> map) {
		VelocityContext velocityContext = new VelocityContext();
		
		Iterator<Map.Entry<String, Object>> entries = map.entrySet().iterator(); 
		while (entries.hasNext()) { 
		  Map.Entry<String, Object> entry = entries.next(); 
		  velocityContext.put(entry.getKey(), entry.getValue());
		}
		
		sendByTemp(subject, receiveUser, path, velocityContext);
	}

	@Override
	public void sendByTemp(String subject, String receiveUser, String path, 
			VelocityContext vc){
		if(velocityEngine == null){
			log.warn("未开启邮件模板功能......");
			return;
		}
        StringWriter stringWriter = new StringWriter();
        Template template = velocityEngine.getTemplate(path, "UTF-8");
        template.merge(vc, stringWriter);
        send(subject, stringWriter.toString(), null, receiveUser, null, null, true);
	}
}
