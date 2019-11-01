package com.liuzi.rocketmq.config;


import javax.annotation.PostConstruct;

import lombok.Data;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;
import org.springframework.util.StringUtils;

import com.liuzi.rocketmq.bean.RocketConsumerLifecycleProcessor;
import com.liuzi.rocketmq.bean.RocketListenerAnnotationBeanPostProcessor;
import com.liuzi.rocketmq.bean.RocketListenerAnnotationPostProcessor;
import com.liuzi.rocketmq.util.Constant;

@Data
public class RocketContextConfig {
	
	private String topic;
	private String namesrvAddr;
	private String producerGroup;
	private String consumerSuffix;
	
	@PostConstruct
	public void init(){
		if(StringUtils.isEmpty(topic)){
			throw new IllegalArgumentException("topic is null");
		}
		Constant._TOPIC = topic;
		Constant.ROCKETMQ_CONSUMER_BEAN_NAME_SUFFIX = consumerSuffix;
	}
	
	@Bean
    public DefaultMQProducer rocketMQProducer() throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer();
        producer.setProducerGroup(producerGroup);
        producer.setNamesrvAddr(namesrvAddr);
        //producer.setInstanceName(instanceName);
        // 必须设为false否则连接broker10909端口
        //producer.setVipChannelEnabled(false);
        producer.start();
        return producer;
    }
	
	@Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public RocketListenerAnnotationPostProcessor rocketListenerAnnotationProcessor() {
        return new RocketListenerAnnotationPostProcessor();
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public RocketListenerAnnotationBeanPostProcessor rocketListenerAnnotationBeanPostProcessor() {
        return new RocketListenerAnnotationBeanPostProcessor();
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public RocketConsumerLifecycleProcessor rocketConsumerLifecycleProcessor() {
        return new RocketConsumerLifecycleProcessor();
    }
}
