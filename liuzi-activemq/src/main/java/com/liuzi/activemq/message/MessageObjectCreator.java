package com.liuzi.activemq.message;

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.core.MessageCreator;

public class MessageObjectCreator implements MessageCreator {
	
	private Serializable object;

	public MessageObjectCreator(Serializable object) {
		super();
		this.object=object;
	}

	@Override
	public Message createMessage(Session session) throws JMSException {
		
		return session.createObjectMessage(object);
	}
}
