package com.liuzi.activemq.producer.impl;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.liuzi.activemq.message.MessageMapCreator;
import com.liuzi.activemq.message.MessageObjectCreator;
import com.liuzi.activemq.message.MessageTextCreator;
import com.liuzi.activemq.producer.ProducerService;

@Service("producerService")
public class ProducerServiceImpl implements ProducerService{
	
	@Resource
	private JmsTemplate jmsTopicTemplate;
	
	@Resource
	private JmsTemplate jmsQueueTemplate;

	private static ActiveMQQueue queueDestination = new ActiveMQQueue();
	private static ActiveMQTopic topicDestination = new ActiveMQTopic();
	
	/**
	 * 向默认队列发送String
	 */
    public void sendTopic(String msg) {
    	String destination =  jmsTopicTemplate.getDefaultDestination().toString();
    	System.out.println("向队列" + destination + "发送了消息------------" + msg);
    	MessageCreator mc = new MessageTextCreator(msg);
    	jmsTopicTemplate.send(mc);
    }
    
    /**
	 * 向默认队列发送String
	 */
    public void sendQueue(String msg) {
    	String destination =  jmsTopicTemplate.getDefaultDestination().toString();
    	System.out.println("向队列" + destination + "发送了消息------------" + msg);
    	MessageCreator mc = new MessageTextCreator(msg);
    	jmsQueueTemplate.send(mc);
    }
    
    /** 
     * 向指定队列发送String
     */  
    public void sendTopic(String physicalName, String msg) {
    	topicDestination.setPhysicalName(physicalName);
    	System.out.println("向队列" + topicDestination.toString() + "发送了消息------------" + msg);  
    	MessageCreator mc = new MessageTextCreator(msg);
    	jmsTopicTemplate.send(topicDestination, mc);
    } 
    
    /** 
     * 向指定队列发送String
     */  
    public void sendQueue(String physicalName, String msg) { 
    	queueDestination.setPhysicalName(physicalName);
    	System.out.println("向队列" + queueDestination.toString() + "发送了消息------------" + msg);  
    	MessageCreator mc = new MessageTextCreator(msg);
    	jmsQueueTemplate.send(queueDestination, mc);
    } 
    
	/**
     * 向默认队列发送对象
     */
	public void sendTopic(Serializable serializable) {
    	String destination =  jmsTopicTemplate.getDefaultDestination().toString();
        System.out.println("向队列" +destination+ "发送了消息------------" + serializable);
        MessageCreator mc = new MessageObjectCreator(serializable);
        jmsTopicTemplate.send(mc);
	}
	
	/**
     * 向默认队列发送对象
     */
	public void sendQueue(Serializable serializable) {
    	String destination =  jmsTopicTemplate.getDefaultDestination().toString();
        System.out.println("向队列" +destination+ "发送了消息------------" + serializable);
        MessageCreator mc = new MessageObjectCreator(serializable);
        jmsQueueTemplate.send(mc);
	}
    
    /** 
     * 向指定队列发送对象
     */  
    public void sendTopic(String physicalName, Serializable serializable) {  
    	topicDestination.setPhysicalName(physicalName);
    	System.out.println("向队列" + topicDestination.toString() + "发送了消息------------" + serializable);  
    	MessageObjectCreator mc = new MessageObjectCreator(serializable);
    	jmsTopicTemplate.send(topicDestination, mc);
    } 
    
    /** 
     * 向指定队列发送对象
     */  
    public void sendQueue(String physicalName, Serializable serializable) {  
    	queueDestination.setPhysicalName(physicalName);
    	System.out.println("向队列" + queueDestination.toString() + "发送了消息------------" + serializable);  
    	MessageObjectCreator mc = new MessageObjectCreator(serializable);
    	jmsQueueTemplate.send(queueDestination, mc);
    } 
     
     
	/**
	 * 向默认队列发送map
	 */
	public void sendTopic(Map<String,Object> map) {
		String destination =  jmsTopicTemplate.getDefaultDestination().toString();
		System.out.println("向队列" +destination+ "发送了消息------------" + map);
		MessageCreator mc = new MessageMapCreator(map);
		jmsTopicTemplate.send(mc);
	}
	
	/**
	 * 向默认队列发送map
	 */
	public void sendQueue(Map<String,Object> map) {
		String destination =  jmsTopicTemplate.getDefaultDestination().toString();
		System.out.println("向队列" +destination+ "发送了消息------------" + map);
		MessageCreator mc = new MessageMapCreator(map);
		jmsQueueTemplate.send(mc);
	}
	
	/** 
     * 向指定队列发送map
     */  
    public void sendTopic(String physicalName, Map<String,Object> map) {  
    	topicDestination.setPhysicalName(physicalName);
    	System.out.println("向队列" + topicDestination.toString() + "发送了消息------------" + map);
    	MessageCreator mc = new MessageMapCreator(map);
    	jmsTopicTemplate.send(topicDestination, mc);
    } 

    /** 
     * 向指定队列发送map
     */  
    public void sendQueue(String physicalName, Map<String,Object> map) {  
    	queueDestination.setPhysicalName(physicalName);
    	System.out.println("向队列" + queueDestination.toString() + "发送了消息------------" + map);
    	MessageCreator mc = new MessageMapCreator(map);
    	jmsQueueTemplate.send(queueDestination, mc);
    } 
}
