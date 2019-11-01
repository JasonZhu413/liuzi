package com.liuzi.rocketmq.util;

import java.util.Date;
import java.util.List;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import com.liuzi.util.common.Log;
import com.liuzi.util.date.DateUtil;


public class CTest {
	
	public static void main(String[] args) throws Exception {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(Configure.CONSUMER_GROUP);
		consumer.setNamesrvAddr(Configure.NAMESRV_ADDR);
		consumer.setMessageModel(MessageModel.CLUSTERING);
		consumer.subscribe(Configure.MESSAGE_TOPIC, Configure.MESSAGE_TAG_ORDER_1 + " || " + 
				Configure.MESSAGE_TAG_ORDER_2);
	 
		consumer.registerMessageListener(new MessageListenerOrderly() {
			@Override
			public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
				System.out.println("haha");
				System.out.println(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
				//手动确认
				//context.setAutoCommit(false);
				msgs.forEach(m->{
					System.out.print("host:"+m.getBornHost()+"--");
					System.out.print("key:"+m.getKeys()+"--");
					System.out.print("Topic:"+m.getTopic()+"--");
					System.out.print("QueueId:"+m.getQueueId()+"--");
					System.out.print("tags:"+m.getTags()+"--");
					System.out.print("msg:"+new String(m.getBody()));
					System.out.println();
				});
				return ConsumeOrderlyStatus.SUCCESS;
	 
			}
		});
	 
		consumer.start();
	 
		Log.info("Consumer Started.%n");
    }

}
