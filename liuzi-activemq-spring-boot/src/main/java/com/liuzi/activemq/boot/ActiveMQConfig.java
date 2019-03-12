package com.liuzi.activemq.boot;



import lombok.extern.slf4j.Slf4j;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

import com.liuzi.util.common.LiuziUtil;


@Slf4j
@Configuration
public class ActiveMQConfig{
	
    @Value("${activemq.maximumRedeliveries}")
    private int maximumRedeliveries;
    @Value("${activemq.initialRedeliveryDelay}")
    private int initialRedeliveryDelay;
    @Value("${activemq.backOffMultiplier}")
    private int backOffMultiplier;
    @Value("${activemq.maximumRedeliveryDelay}")
    private int maximumRedeliveryDelay;
    @Value("${activemq.redeliveryDelay}")
    private int redeliveryDelay;
    
    @Value("${activemq.brokerURL}")
    private String brokerURL;
    @Value("${activemq.username}")
    private String username;
    @Value("${activemq.password}")
    private String password;
    @Value("${activemq.clientID}")
    private String clientID;
    
    @Value("${activemq.receiveTimeout}")
    private int receiveTimeout;
    @Value("${activemq.explicitQosEnabled}")
    private boolean explicitQosEnabled;
    @Value("${activemq.deliveryMode}")
    private int deliveryMode;
    
	@Bean
    public JmsTemplate jmsTemplate(){
    	LiuziUtil.tag("--------  Liuzi ActiveMQ初始化   --------");
    	log.info("--------  Liuzi ActiveMQ初始化，注入 jmsTemplate   --------");
    	
    	RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
		redeliveryPolicy.setUseExponentialBackOff(true);//是否在每次尝试重新发送失败后,增长这个等待时间
		redeliveryPolicy.setMaximumRedeliveries(maximumRedeliveries);//重发次数
		redeliveryPolicy.setInitialRedeliveryDelay(initialRedeliveryDelay);//重发时间间隔
		//第一次失败后重新发送之前等待500毫秒,第二次失败再等待500 * 2毫秒,backOffMultiplier就是value
		redeliveryPolicy.setBackOffMultiplier(backOffMultiplier);
		//最大传送延迟，只在useExponentialBackOff为true时有效（V5.5），假设首次重连间隔为10ms，倍数为2，那么第
		//二次重连时间间隔为 20ms，第三次重连时间间隔为40ms，当重连时间间隔大的最大重连时间间隔时，以后每次重连时间间隔都为最大重连时间间隔。
		redeliveryPolicy.setMaximumRedeliveryDelay(maximumRedeliveryDelay);
		redeliveryPolicy.setRedeliveryDelay(redeliveryDelay);
		
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(username, password, brokerURL);
		activeMQConnectionFactory.setUseAsyncSend(true);
		activeMQConnectionFactory.setRedeliveryPolicy(redeliveryPolicy);
		//activeMQConnectionFactory.setClientID(clientID);
		
		//schedulePeriodForDestinationPurge = 3600000，表示每一小时检查一次，默认为 0，此功能关闭
		//gcInactiveDestinations，true 表示删除回收闲置的队列，默认为 false
		//inactiveTimoutBeforeGC = 600000，表示当队列或主题闲置 10 分钟后被删除，默认为 60 秒。
		
		JmsTemplate jmsTemplate = new JmsTemplate(activeMQConnectionFactory);
		jmsTemplate.setReceiveTimeout(receiveTimeout);
		//jmsTemplate.setTimeToLive(timeToLive);
		jmsTemplate.setExplicitQosEnabled(explicitQosEnabled);
		jmsTemplate.setDeliveryMode(deliveryMode);
		
		log.info("--------  Liuzi ActiveMQ初始化完成   --------");
		
		return jmsTemplate;
    }
	
	/*@Bean(name="topicTemplate")
    public JmsTemplate topicTemplate(){
		log.info("--------  topicTemplate注入   --------");
		
		JmsTemplate topicTemplate = new JmsTemplate(activeMQConnectionFactory);
		topicTemplate.setPubSubDomain(true);
		topicTemplate.setReceiveTimeout(receiveTimeout);
		//topicTemplate.setTimeToLive(timeToLive);
		topicTemplate.setExplicitQosEnabled(explicitQosEnabled);
		topicTemplate.setDeliveryMode(deliveryMode);
		return topicTemplate;
    }
	
	@Bean(name="queueTemplate")
    public JmsTemplate queueTemplate(){
		log.info("--------  queueTemplate注入   --------");
		JmsTemplate queueTemplate = new JmsTemplate(activeMQConnectionFactory);
		queueTemplate.setPubSubDomain(false);
		queueTemplate.setReceiveTimeout(receiveTimeout);
		queueTemplate.setExplicitQosEnabled(explicitQosEnabled);
		queueTemplate.setDeliveryMode(deliveryMode);
		return queueTemplate;
    }*/
}
