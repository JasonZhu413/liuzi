package com.liuzi.activemq.consumer;

import javax.jms.BytesMessage;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;

import org.springframework.stereotype.Component;

import com.liuzi.util.common.Log;



@Component
public class OnMessage implements MessageListener{
	 
	@Override
	public void onMessage(Message m){
		try {
			//ActiveMQDestination activeMQDestination = (ActiveMQDestination) m.getJMSDestination();  
			//TextMessage textMessage = (TextMessage) message;  
			//log.info("消费者消费carinfosyncQueue队列：文本消息" + textMessage.getText());
			if(m instanceof TextMessage){ //接收文本消息   
				TextMessage message = (TextMessage) m;
	            Log.info("消费者消费(文本消息 ): {}", message.getText());   
	        }else if(m instanceof MapMessage){ //接收键值对消息   
	            MapMessage message = (MapMessage)m;   
	            Log.info("消费者消费(键值对消息 ): {}", message);
	        }else if(m instanceof StreamMessage){ //接收流消息   
	            StreamMessage message = (StreamMessage) m;   
	            Log.info("消费者消费(流消息 ): {}", message.readString());   
	            Log.info("消费者消费(流消息 ): {}", message.readLong());   
	        }else if(m instanceof BytesMessage){ //接收字节消息   
	            byte[] b = new byte[1024];   
	            int len = -1;   
	            BytesMessage message = (BytesMessage) m;   
	            while((len = message.readBytes(b)) != -1){   
	            	Log.info("消费者消费(字节消息 ): {}", new String(b, 0, len));
	            }
	        }else if(m instanceof ObjectMessage){ //接收对象消息   
	            ObjectMessage message = (ObjectMessage) m;   
	            Log.info("消费者消费(对象消息 ): {}", message);   
	        }else{   
	        	Log.info("消费者消费(其他消息 ): {}", m);
	        }   
		} catch (Exception e) {
			Log.error(e, "Activemq 监听系统错误");
		}
	}
}
