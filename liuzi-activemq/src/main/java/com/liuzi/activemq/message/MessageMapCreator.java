package com.liuzi.activemq.message;

import java.util.Iterator;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.core.MessageCreator;

public class MessageMapCreator implements MessageCreator {
	
	private Map<String,Object> map;

	public MessageMapCreator(Map<String,Object> map) {
		super();
		this.map=map;
	}

	@Override
	public Message createMessage(Session session) throws JMSException {
		MapMessage mm = session.createMapMessage();
		Iterator<String> it = map.keySet().iterator();
		String key;
		Object value;
		while(it.hasNext()){
			key = it.next();
			value = map.get(key);
			mm.setObject(key, value);
		}
		
		return mm;
	}

}
