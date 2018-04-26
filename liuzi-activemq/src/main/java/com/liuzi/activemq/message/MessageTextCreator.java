package com.liuzi.activemq.message;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.core.MessageCreator;

public class MessageTextCreator implements MessageCreator {
	
	private String mes;

	public MessageTextCreator(String mes) {
		super();
		this.mes = mes;
	}

	@Override
	public Message createMessage(Session session) throws JMSException {
		
		return session.createTextMessage(mes);
	}

}
