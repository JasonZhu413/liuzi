package com.liuzi.activemq.producer;

import java.io.Serializable;
import java.util.Map;


public interface ProducerService {
	
	/**
	 * 向默认队列发送String
	 */
    public void sendTopic(String msg);
    
    /**
	 * 向默认队列发送String
	 */
    public void sendQueue(String msg);
    
    /** 
     * 向指定队列发送String
     */  
    public void sendTopic(String physicalName, String msg);
    
    /** 
     * 向指定队列发送String
     */  
    public void sendQueue(String physicalName, String msg);
	/**
     * 向默认队列发送对象
     */
	public void sendTopic(Serializable serializable);
	
	/**
     * 向默认队列发送对象
     */
	public void sendQueue(Serializable serializable);
    
    /** 
     * 向指定队列发送对象
     */  
    public void sendTopic(String physicalName, Serializable serializable);
    
    /** 
     * 向指定队列发送对象
     */  
    public void sendQueue(String physicalName, Serializable serializable);
     
     
	/**
	 * 向默认队列发送map
	 */
	public void sendTopic(Map<String,Object> map);
	
	/**
	 * 向默认队列发送map
	 */
	public void sendQueue(Map<String,Object> map) ;
	
	/** 
     * 向指定队列发送map
     */  
    public void sendTopic(String physicalName, Map<String,Object> map);

    /** 
     * 向指定队列发送map
     */  
    public void sendQueue(String physicalName, Map<String,Object> map);
}
