package com.liuzi.rocketmq.util;

import java.util.Date;
import java.util.List;

import lombok.Getter;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import com.liuzi.util.common.Log;
import com.liuzi.util.date.DateUtil;

public class PTest {
	
	public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer(Configure.BROKER_GROUP);
        //"192.168.100.145:9876;192.168.100.146:9876;192.168.100.149:9876;192.168.100.239:9876"
    	producer.setNamesrvAddr(Configure.NAMESRV_ADDR);
    	
    	// 设置实例名称
        //producer.setInstanceName("quick_start_producer");
        // 设置重试次数,默认2
        //producer.setRetryTimesWhenSendFailed(3);
        //设置发送超时时间，默认是3000
        //producer.setSendMsgTimeout(1);
        // 开启生产者
        producer.start();
     
    	String[] tags = new String[] {Configure.MESSAGE_TAG_ORDER_1, Configure.MESSAGE_TAG_ORDER_2};
    	int i = 0;
    	//for (int i = 0; i < 100; i++) {
    		int orderId = i % 10;
     
    		Log.info("orderId {}", orderId);
    		
    		//创建消息
    		//String topic, String tags, String keys, byte[] body)
    		Message msg = new Message(Configure.MESSAGE_TOPIC, tags[i % tags.length], "KEY" + i,
    				("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
    		msg.setDelayTimeLevel(3);
    		//发送消息，重写选择MessageQueue 方法，把消息写到对应的ConsumerQueue 中
    		// orderId 参数传递到内部方法 select arg 参数
    		SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
    			@Override
    			public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
    			   Log.info("arg {}",arg);
    				T t = (T) arg;
    				int index = t.getOrderId() % mqs.size();
    				//返回选中的队列
    				return mqs.get(index);
    			}
    		}, new T(orderId));
    		System.out.println(DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
    		System.out.printf("%s%n", sendResult);
    	//}
    	//关闭
    	producer.shutdown();
    }
	
	static class T{
		@Getter int orderId;
		T(int i){
			orderId = i;
		}
	}

}
