package com.liuzi.qmail;

import java.util.Date;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import lombok.extern.slf4j.Slf4j;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Template;


@Slf4j
public class QMail extends QmailConfig{
	
	private JavaMailSender javaMailSender;
    private SimpleMailMessage simpleMailMessage;
    private FreeMarkerConfigurer freeMarkerConfigurer;
	
    /**
     * 发送邮件(模板)
     * @param to 接收方邮箱
     * @param path 模板地址
     * @param map 参数
     */
    public void sendByTemp(String to, String path, Map<String, String> map) {
    	sendByTemp(null, to, null, null, path, map);
    }
    
    /**
     * 发送邮件(模板)
     * @param to 接收方邮箱
     * @param subject 邮件主题
     * @param path 模板地址
     * @param map 参数
     */
    public void sendByTemp(String to, String subject, String path, Map<String, String> map) {
    	sendByTemp(null, to, subject, null, path, map);
    }
    
    /**
     * 发送邮件(模板)
     * @param to 接收方邮箱
     * @param date 发送时间
     * @param path 模板地址
     * @param map 参数
     */
    public void sendByTemp(String to, Date date, String path, Map<String, String> map) {
    	sendByTemp(null, to, null, date, path, map);
    }
    
    
	
    /**
     * 发送邮件(模板)
     * @param from 发送方邮箱
     * @param to 接收方邮箱
     * @param subject 邮件主题
     * @param date 发送时间
     * @param path 模板地址
     * @param map 参数
     */
    public void sendByTemp(String from, String to, String subject, Date date, String path, 
    		Map<String, String> map) {
    	if(StringUtils.isEmpty(path)){
    		log.warn("发送邮件(模板)错误, 模板地址为空!");
    		return;
    	}
        String text = "";
        try {
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate(path);
            text = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        send(from, to, subject, date, text);
    }
    
    /**
     * 发送邮件
     * @param to 收件人邮箱
     * @param content 邮件内容
     */
    public void send(String to, String content) {
    	send(null, to, null, null, content);
    }
    
    /**
     * 发送邮件
     * @param to 收件人邮箱
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    public void send(String to, String subject, String content) {
    	send(null, to, subject, null, content);
    }
    
    /**
     * 发送邮件
     * @param to 收件人邮箱
     * @param date 发送时间
     * @param content 邮件内容
     */
    public void send(String to, Date date, String content) {
    	send(null, to, null, date, content);
    }
    
    /**
     * 发送邮件
     * @param to 收件人邮箱
     * @param subject 邮件主题
     * @param date 发送时间
     * @param content 邮件内容
     */
    public void send(String to, String subject, Date date, String content) {
    	send(null, to, subject, date, content);
    }
    
    /**
     * 发送邮件
     * @param from 发件人邮箱
     * @param to 收件人邮箱
     * @param subject 邮件主题
     * @param date 发送时间
     * @param content 邮件内容
     */
    public void send(String from, String to, String subject, Date date, String content) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
            if(StringUtils.isEmpty(from)){
            	from = simpleMailMessage.getFrom();
            }
            messageHelper.setFrom(simpleMailMessage.getFrom());
            if (StringUtils.isEmpty(subject)) {
            	subject = simpleMailMessage.getSubject();
            }
            messageHelper.setSubject(subject);
            if(date == null){
            	date = new Date();
            }
            messageHelper.setSentDate(date);
            messageHelper.setTo(to);
            messageHelper.setText(content, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
