package com.liuzi.activemq.consumer;

import javax.jms.MessageListener;

import lombok.extern.slf4j.Slf4j;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.liuzi.util.common.Log;


@Component
public class ConsumerListener {

	@Autowired
	private ActiveMQConnectionFactory connectionFactory;
	@Autowired
	private ActiveMQQueue activeMQQueue;
	@Autowired
	private ActiveMQTopic activeMQTopic;
	
	@Pointcut("@annotation(com.liuzi.activemq.consumer.Consumer)")
	private void listenerPointCut() {}
	
	@SuppressWarnings("unchecked")
	@Before("listenerPointCut()")
	public void listener(JoinPoint joinPoint){  
        Object[] args = joinPoint.getArgs();
        
        //监听类
        MessageListener messageListener = null;
        try {
        	Object obj = args[2];
        	if(obj instanceof OnMessage){
        		Log.error("Create consumer listener error, listener is not create");
        		return;
        	}
        	Class<? extends MessageListener> clazz = (Class<? extends MessageListener>) obj;
        	messageListener = clazz.newInstance();
        	if(messageListener == null){
        		Log.error("Create consumer listener error, listener instance fail");
            	return;
            }
		} catch (Exception e) {
			Log.error(e, "Create consumer listener system error");
			return;
		}
        
        //参数队列名称
        Object args0 = args[0];
        //消息类型
        ActiveMQDestination activeMQDestination = (boolean) args[1] ? activeMQQueue : activeMQTopic;
        //默认队列名称
        String defaultName = activeMQDestination.getPhysicalName();
        
        //创建监听
        String[] names = new String[1];
        if(StringUtils.isEmpty(args0)){
        	names[0] = defaultName;
        }else if(args0 instanceof String){
        	names[0] = args0.toString();
        }else{
        	names = (String[]) args0;
        }
        
        for(String name : names){
        	createListener(name, activeMQDestination, messageListener);
        }
        
        Log.info("Create consumer listener success, listener size: {}", names.length);
    }
	
	private void createListener(String name, ActiveMQDestination activeMQDestination, 
			MessageListener messageListener){
        DefaultMessageListenerContainer listenerContainer = new DefaultMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactory);
        listenerContainer.setMessageListener(messageListener);
        listenerContainer.setSessionTransacted(false);
        activeMQDestination.setPhysicalName(name);
    	listenerContainer.setDestination(activeMQDestination);
	}
}
