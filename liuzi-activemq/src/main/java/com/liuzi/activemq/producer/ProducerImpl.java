package com.liuzi.activemq.producer;

import java.io.Serializable;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.liuzi.activemq.message.MessageTextCreator;
import com.liuzi.util.common.Result;



@Service("producer")
public class ProducerImpl implements Producer{
	
	@Autowired
	private JmsTemplate topicTemplate;
	@Autowired
	private JmsTemplate queueTemplate;
	@Autowired
	private ActiveMQQueue activeMQQueue;
	@Autowired
	private ActiveMQTopic activeMQTopic;
	
	/**
	 * 向默认topic发送String
	 */
    public void sendTopic(String msg) {
    	sendTopic_object(msg);
    }
    /** 
     * 向指定topic发送String
     */  
    public void sendTopic(String physicalName, String msg) {
    	sendTopic_object(physicalName, msg);
    } 
	/**
     * 向默认topic发送对象
     */
	public void sendTopic(Serializable serializable) {
		sendTopic_object(serializable);
	}
    /** 
     * 向指定topic发送对象
     */  
    public void sendTopic(String physicalName, Serializable serializable) { 
    	sendTopic_object(physicalName, serializable);
    } 
    /** 
     * 向指定topic发送对象
     */  
    public void sendTopic(String physicalName, Result res) { 
    	sendTopic_object(physicalName, res);
    } 
   	/**
   	 * 向默认topic发送map
   	 */
   	public void sendTopic(Map<String,Object> map) {
   		sendTopic_object(map);
   	}
    /** 
     * 向指定topic发送map
     */  
    public void sendTopic(String physicalName, Map<String,Object> map) { 
    	sendTopic_object(physicalName, map);
    } 
    /** 
     * 向指定topic发送
     */  
    private void sendTopic_object(Object object) { 
    	String destination =  topicTemplate.getDefaultDestination().toString();
   		System.out.println("向队列" +destination+ "发送了消息------------" + object);
   		//Object obj = JSONObject.toJSON(object);
    	MessageCreator mc = new MessageTextCreator(object.toString());
   		topicTemplate.send(mc);
    } 
    /** 
     * 向指定topic发送
     */  
    private void sendTopic_object(String physicalName, Object object) { 
    	activeMQTopic.setPhysicalName(physicalName);
    	System.out.println("向队列" + activeMQTopic.toString() + "发送了消息------------" + object);
    	//Object obj = JSONObject.toJSON(object);
    	MessageCreator mc = new MessageTextCreator(object.toString());
    	topicTemplate.send(activeMQTopic, mc);
    } 
    
    /**
	 * 向默认queue发送String
	 */
    public void sendQueue(String msg) {
    	sendQueue_object(msg);
    }
    /** 
     * 向指定queue发送String
     */  
    public void sendQueue(String physicalName, String msg) {
    	sendQueue_object(physicalName, msg);
    } 
	/**
     * 向默认queue发送对象
     */
	public void sendQueue(Serializable serializable) {
		sendQueue_object(serializable);
	}
    /** 
     * 向指定queue发送对象
     */  
    public void sendQueue(String physicalName, Serializable serializable) {
    	sendQueue_object(physicalName, serializable);
    } 
	/**
	 * 向默认queue发送map
	 */
	public void sendQueue(Map<String,Object> map) {
		sendQueue_object(map);
	}
    /** 
     * 向指定queue发送map
     */  
    public void sendQueue(String physicalName, Map<String,Object> map) { 
    	sendQueue_object(physicalName, map);
    } 
    /**
	 * 向默认queue发送
	 */
	public void sendQueue_object(Object object) {
		String destination =  topicTemplate.getDefaultDestination().toString();
		System.out.println("向队列" +destination+ "发送了消息------------" + object);
		//Object obj = JSONObject.toJSON(object);
    	MessageCreator mc = new MessageTextCreator(object.toString());
		queueTemplate.send(mc);
	}
    /** 
     * 向指定queue发送
     */  
    private void sendQueue_object(String physicalName, Object object) { 
    	activeMQQueue.setPhysicalName(physicalName);
    	System.out.println("向队列" + activeMQQueue.toString() + "发送了消息------------" + object);
    	
    	//Object obj = JSONObject.toJSON(object);
    	MessageCreator mc = new MessageTextCreator(object.toString());
    	queueTemplate.send(activeMQQueue, mc);
    } 
    
    public TopicSubscriber createTopicConsumer(String clientID, Topic topic, String name,
    		MessageListener listener){
    	
    	TopicSubscriber consumer = null;
    	try{
    		Connection conn = topicTemplate.getConnectionFactory().createConnection();
        	conn.setClientID(clientID);
        	conn.start();
        	
        	Session session = conn.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
        	consumer = session.createDurableSubscriber(topic, name);
        	consumer.setMessageListener(listener);
    	}catch (Exception e) {
			System.out.println("create consumer error");
		}
    	
    	return consumer;
    } 
}
