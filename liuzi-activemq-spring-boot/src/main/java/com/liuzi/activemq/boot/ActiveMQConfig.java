package com.liuzi.activemq.boot;



import lombok.extern.slf4j.Slf4j;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

import com.liuzi.util.LiuziUtil;

@Slf4j
@Configuration
public class ActiveMQConfig{
	
    @Value("${activemq.maximumRedeliveries}")
    private String maximumRedeliveries;
    @Value("${activemq.initialRedeliveryDelay}")
    private String initialRedeliveryDelay;
    @Value("${activemq.backOffMultiplier}")
    private String backOffMultiplier;
    @Value("${activemq.maximumRedeliveryDelay}")
    private String maximumRedeliveryDelay;
    @Value("${activemq.redeliveryDelay}")
    private String redeliveryDelay;
    
    @Value("${activemq.brokerURL}")
    private String brokerURL;
    @Value("${activemq.userName}")
    private String userName;
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
    
    private ActiveMQConnectionFactory activeMQConnectionFactory;
	private RedeliveryPolicy redeliveryPolicy;
    
    public ActiveMQConfig(){
    	LiuziUtil.tag("  --------  Liuzi ActiveMQ初始化......  --------");
    	
    	redeliveryPolicy = new RedeliveryPolicy();
		redeliveryPolicy.setUseExponentialBackOff(true);//是否在每次尝试重新发送失败后,增长这个等待时间
		redeliveryPolicy.setMaximumRedeliveries(Integer.parseInt(maximumRedeliveries));//重发次数
		redeliveryPolicy.setInitialRedeliveryDelay(Integer.parseInt(initialRedeliveryDelay));//重发时间间隔
		//第一次失败后重新发送之前等待500毫秒,第二次失败再等待500 * 2毫秒,backOffMultiplier就是value
		redeliveryPolicy.setBackOffMultiplier(Integer.parseInt(backOffMultiplier));
		//最大传送延迟，只在useExponentialBackOff为true时有效（V5.5），假设首次重连间隔为10ms，倍数为2，那么第
		//二次重连时间间隔为 20ms，第三次重连时间间隔为40ms，当重连时间间隔大的最大重连时间间隔时，以后每次重连时间间隔都为最大重连时间间隔。
		redeliveryPolicy.setMaximumRedeliveryDelay(Integer.parseInt(maximumRedeliveryDelay));
		redeliveryPolicy.setRedeliveryDelay(Integer.parseInt(redeliveryDelay));
		
		activeMQConnectionFactory = new ActiveMQConnectionFactory(userName, password, brokerURL);
		activeMQConnectionFactory.setUseAsyncSend(true);
		activeMQConnectionFactory.setRedeliveryPolicy(redeliveryPolicy);
		//activeMQConnectionFactory.setClientID(clientID);
		
		//schedulePeriodForDestinationPurge = 3600000，表示每一小时检查一次，默认为 0，此功能关闭
		//gcInactiveDestinations，true 表示删除回收闲置的队列，默认为 false
		//inactiveTimoutBeforeGC = 600000，表示当队列或主题闲置 10 分钟后被删除，默认为 60 秒。
    }
    
	
	@Bean(name="topicTemplate")
    public JmsTemplate topicTemplate(){
		JmsTemplate topicJmsTemplate = new JmsTemplate(activeMQConnectionFactory);
		topicJmsTemplate.setPubSubDomain(true);
		topicJmsTemplate.setReceiveTimeout(receiveTimeout);
		//topicJmsTemplate.setTimeToLive(timeToLive);
		topicJmsTemplate.setExplicitQosEnabled(explicitQosEnabled);
		topicJmsTemplate.setDeliveryMode(deliveryMode);
		return topicJmsTemplate;
    }
	
	@Bean(name="queueJmsTemplate")
    public JmsTemplate queueTemplate(){
		JmsTemplate queueJmsTemplate = new JmsTemplate(activeMQConnectionFactory);
		queueJmsTemplate.setPubSubDomain(false);
		queueJmsTemplate.setReceiveTimeout(receiveTimeout);
		queueJmsTemplate.setExplicitQosEnabled(explicitQosEnabled);
		queueJmsTemplate.setDeliveryMode(deliveryMode);
		return queueJmsTemplate;
    }
}
