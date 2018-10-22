package com.liuzi.activemq;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.util.StringUtils;

import com.liuzi.util.LiuziUtil;

@Configuration
public class ActiveMQConfig{
	
	private static Logger logger = LoggerFactory.getLogger(ActiveMQConfig.class);
	
	private final static String DEFAULT_CONF_FILE_NAME = "conf/activemq.properties";
	
	private static String g_conf_file = DEFAULT_CONF_FILE_NAME;
	private static Properties properties;
	
	private static ActiveMQConnectionFactory activeMQConnectionFactory;
	private static RedeliveryPolicy redeliveryPolicy;
	
    public JmsTemplate topicJmsTemplate;
    public JmsTemplate queueJmsTemplate;
	
	@Bean
    public JmsTemplate topicTemplate(){
		logger.info("topicTemplate 注入:" + (topicJmsTemplate != null));
		return topicJmsTemplate;
    }
	
	@Bean
    public JmsTemplate queueTemplate(){
		logger.info("queueJmsTemplate 注入 :" + (topicJmsTemplate != null));
		return queueJmsTemplate;
    }
	
	public ActiveMQConfig(){
		init();
	}
	
	public ActiveMQConfig(String confFile){
		if(!StringUtils.isEmpty(confFile)){
			g_conf_file = confFile;
		}
		init();
	}

	public void init() {
		LiuziUtil.tag("  --------  Liuzi ActiveMQ初始化......  --------");
		
		logger.info("======== activemq初始化，加载配置" + g_conf_file + " ========");
		
		if(properties == null){
			try (InputStream in = PropertyUtils.class.getClassLoader().getResourceAsStream(g_conf_file)){
				properties = new Properties();
				properties.load(in);
			} catch (IOException e) {
				logger.error("activemq初始化失败，错误：" + e.getMessage());
				e.printStackTrace();
				return;
			}
		}
		
		try {
			
			String maximumRedeliveries = properties.getProperty("activemq.maximumRedeliveries");
			String initialRedeliveryDelay = properties.getProperty("activemq.initialRedeliveryDelay");
			String backOffMultiplier = properties.getProperty("activemq.backOffMultiplier");
			String maximumRedeliveryDelay = properties.getProperty("activemq.maximumRedeliveryDelay");
			String redeliveryDelay = properties.getProperty("activemq.redeliveryDelay");
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
			
			String brokerURL = properties.getProperty("activemq.brokerURL");
			String userName = properties.getProperty("activemq.userName");
			String password = properties.getProperty("activemq.password");
			String clientID = properties.getProperty("activemq.clientID");
			activeMQConnectionFactory = new ActiveMQConnectionFactory(userName, password, brokerURL);
			activeMQConnectionFactory.setUseAsyncSend(true);
			activeMQConnectionFactory.setRedeliveryPolicy(redeliveryPolicy);
			//activeMQConnectionFactory.setClientID(clientID);
			
			//schedulePeriodForDestinationPurge = 3600000，表示每一小时检查一次，默认为 0，此功能关闭
			//gcInactiveDestinations，true 表示删除回收闲置的队列，默认为 false
			//inactiveTimoutBeforeGC = 600000，表示当队列或主题闲置 10 分钟后被删除，默认为 60 秒。
	        
			String useTopic = properties.getProperty("activemq.use.topic");
			if(!StringUtils.isEmpty(useTopic) && Boolean.parseBoolean(useTopic)){
				String receiveTimeout = properties.getProperty("activemq.topic.receiveTimeout");
				String explicitQosEnabled = properties.getProperty("activemq.topic.explicitQosEnabled");
				String deliveryMode = properties.getProperty("activemq.topic.deliveryMode");
				topicJmsTemplate = new JmsTemplate(activeMQConnectionFactory);
				topicJmsTemplate.setPubSubDomain(true);
				topicJmsTemplate.setReceiveTimeout(Integer.parseInt(receiveTimeout));
				//topicJmsTemplate.setTimeToLive(timeToLive);
				topicJmsTemplate.setExplicitQosEnabled(Boolean.parseBoolean(explicitQosEnabled));
				topicJmsTemplate.setDeliveryMode(Integer.parseInt(deliveryMode));
			}
			
			String useQueue = properties.getProperty("activemq.use.queue");
			if(!StringUtils.isEmpty(useQueue) && Boolean.parseBoolean(useQueue)){
				String receiveTimeout = properties.getProperty("activemq.queue.receiveTimeout");
				String explicitQosEnabled = properties.getProperty("activemq.queue.explicitQosEnabled");
				String deliveryMode = properties.getProperty("activemq.queue.deliveryMode");
				
				queueJmsTemplate = new JmsTemplate(activeMQConnectionFactory);
				queueJmsTemplate.setPubSubDomain(false);
				queueJmsTemplate.setReceiveTimeout(Integer.parseInt(receiveTimeout));
				queueJmsTemplate.setExplicitQosEnabled(Boolean.parseBoolean(explicitQosEnabled));
				topicJmsTemplate.setDeliveryMode(Integer.parseInt(deliveryMode));
			}
			
		} catch (NumberFormatException e) {
			logger.error("activemq初始化失败，错误：" + e.getMessage());
			e.printStackTrace();
			return;
		}
		
		logger.info("======== activemq初始化完成 ========\n");
	}
}
