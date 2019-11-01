package com.liuzi.rocketmq.bean;

import java.beans.PropertyDescriptor;
import java.util.List;



import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.util.StringUtils;

import com.liuzi.rocketmq.util.Constant;
import com.liuzi.util.common.Log;

public class RocketListenerAnnotationBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter {

    @Autowired
    private ConfigurableBeanFactory beanFactory;

    /**
     * 初始化消费者属性
     *
     * @see RocketListener
     */
    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, 
    		Object bean, String beanName) throws BeansException {
        if (bean instanceof DefaultMQPushConsumer) {
            DefaultMQPushConsumer consumer = (DefaultMQPushConsumer) bean;
            consumer.setConsumerGroup(getStringProperty(pvs, Constant.CONSUMER_GROUP));
            String namesrvAddr = getStringProperty(pvs, Constant.NAMESRV_ADDR);
            if (StringUtils.hasText(namesrvAddr)) {
                consumer.setNamesrvAddr(namesrvAddr);
            }
            consumer.setConsumeFromWhere(getProperty(pvs, Constant.CONSUME_FROM_WHERE, 
            		ConsumeFromWhere.class));
            //订阅信息
            String topic = getStringProperty(pvs, Constant.TOPIC);
            String tag = getStringProperty(pvs, Constant.TAG);
            try {
            	topic = StringUtils.isEmpty(topic) ? Constant._TOPIC : topic;
                consumer.subscribe(topic, tag);
            } catch (MQClientException e) {
                throw new BeanCreationException(String.format("订阅基于Topic:[%s],Tag:[%s]的RocketMQ消费者创建失败", topic, tag));
            }
            /**
             * 过时，有问题
             */
            /*Object consumeBean = beanFactory.getBean(getStringProperty(pvs, Constant.CONSUME_BEAN_NAME));
            Method consumeMethod = getProperty(pvs, Constant.CONSUME_METHOD, Method.class);
            if (AopUtils.isAopProxy(consumeBean)) {
                consumeMethod = MethodIntrospector.selectInvocableMethod(consumeMethod, consumeBean.getClass());
            }
            consumer.registerMessageListener(new RocketMessageListener(consumeBean, consumeMethod));*/
            
            consumer.registerMessageListener(new MessageListenerConcurrently() {
				@Override
				public ConsumeConcurrentlyStatus consumeMessage(
						List<MessageExt> msgs,
						ConsumeConcurrentlyContext context) {
					//System.out.printf(Thread.currentThread().getName() + " Receive New Messages: " + msgs + "%n");
					return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
				}
            });
            try {
				consumer.start();
			} catch (MQClientException e) {
				Log.error(e, "consumer start error");
			}
            
            Log.info("订阅ConsumeGroup:[{}],Topic:[{}],Tag:[{}]的RocketMQ消费者创建成功",
            		consumer.getConsumerGroup(), topic, tag);
            return null;
        }
        return super.postProcessPropertyValues(pvs, pds, bean, beanName);
    }

    /**
     * 解析配置,如果配置以"$"开始,则从Properties文件中查找相应的值
     * @param pvs
     * @param propertyName
     * @return
     */
    private String getStringProperty(PropertyValues pvs, String propertyName) {
        String value = (String) pvs.getPropertyValue(propertyName).getValue();
        if (value.startsWith("$")) {
            value = beanFactory.resolveEmbeddedValue(value);
        }
        return value;
    }

    /**
     * @param pvs
     * @return
     */
    @SuppressWarnings("unchecked")
	private <T> T getProperty(PropertyValues pvs, String name, Class<T> targetClass) {
        return (T)pvs.getPropertyValue(name).getValue();
    }
}
