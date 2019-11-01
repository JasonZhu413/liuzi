package com.liuzi.rocketmq.bean;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.MethodIntrospector.MetadataLookup;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import com.liuzi.rocketmq.annotation.RocketListener;
import com.liuzi.rocketmq.util.Constant;
import com.liuzi.util.common.Log;

/**
 * {@link RocketListener}注解解析器
 * @author zsy
 */
public class RocketListenerAnnotationPostProcessor implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) 
    		throws BeansException {
        String[] beanDefinitionNames = beanDefinitionRegistry.getBeanDefinitionNames();
        for (String name : beanDefinitionNames) {
            BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(name);
            Class<?> beanClass;
            try {
                beanClass = resolveBeanClass(beanDefinition, beanDefinitionRegistry);
            } catch (ClassNotFoundException e) {
                throw new BeanDefinitionStoreException(String.format("解析[%s]BeanDefinition出现异常", 
                		beanDefinition.toString()), e);
            }
            Map<Method, RocketListener> annotatedMethods = introspectorRocketAnnotatedMethod(beanClass);
            if (annotatedMethods.isEmpty()) {
                continue;
            }
            for (Map.Entry<Method, RocketListener> entry : annotatedMethods.entrySet()) {
                RocketListener listener = entry.getValue();
                BeanDefinition rocketConsumer = new RootBeanDefinition(DefaultMQPushConsumer.class);
                //让@RocketListener的定义Bean先实例化
                rocketConsumer.setDependsOn(beanDefinition.getFactoryBeanName());
                MutablePropertyValues mpv = rocketConsumer.getPropertyValues();
                mpv.add(Constant.CONSUMER_GROUP, listener.consumerGroup());
                mpv.add(Constant.TOPIC, listener.topic());
                mpv.add(Constant.TAG, listener.tag());
                mpv.add(Constant.NAMESRV_ADDR, listener.namesrvAddr());
                mpv.add(Constant.CONSUME_BEAN_NAME, name);
                mpv.add(Constant.CONSUME_METHOD, entry.getKey());
                mpv.add(Constant.CONSUME_FROM_WHERE, listener.consumeFromWhere());
                //定义消费者Bean的名称格式为：factoryMethodName + RocketConsumer
                beanDefinitionRegistry.registerBeanDefinition(buildRocketConsumerBeanName(entry.getKey().getName()), rocketConsumer);
                Log.info("注册[{}]标识的[{}]方法为RocketMQ Push消费者", RocketListener.class.getSimpleName(),
                		entry.getKey().toString());
            }
        }
    }

    /**
     * 内省{@link RocketListener}标识的方法
     *
     * @param beanClass
     * @return
     */
    private Map<Method, RocketListener> introspectorRocketAnnotatedMethod(Class<?> beanClass) {
        return MethodIntrospector.selectMethods(beanClass,
            (MetadataLookup<RocketListener>)(method) -> method.getAnnotation(RocketListener.class));
    }

    /**
     * 解析Bean Class对象
     *
     * @param beanDefinition
     * @param beanDefinitionRegistry
     * @return
     * @throws ClassNotFoundException
     */
    private Class<?> resolveBeanClass(BeanDefinition beanDefinition, BeanDefinitionRegistry beanDefinitionRegistry) throws ClassNotFoundException {
        String beanClassName = beanDefinition.getBeanClassName();
        Class<?> beanClass = null;
        if (beanClassName != null) {
            beanClass = resolveClass(beanClassName);
        } else if (beanDefinition.getFactoryMethodName() != null) {
            String factoryMethodName = beanDefinition.getFactoryMethodName();
            String factoryBeanClassName = beanDefinitionRegistry.getBeanDefinition(beanDefinition.getFactoryBeanName()).getBeanClassName();
            Class<?> factoryBeanClass = resolveClass(factoryBeanClassName);
            Set<Method> candidateMethods = new HashSet<>();
            ReflectionUtils.doWithMethods(factoryBeanClass,
                candidateMethods::add,
                (method) -> method.getName().equals(factoryMethodName) && method.isAnnotationPresent(Bean.class)
            );
            Assert.state(candidateMethods.size() == 1, String.format("[%s]类中标识@Bean的方法[%s]不止一个", factoryBeanClass.getName(), factoryMethodName));
            beanClass = candidateMethods.iterator().next().getReturnType();
        }
        Assert.notNull(beanClass, String.format("解析[%s]BeanDefinition出现异常,BeanClass解析失败", beanDefinition.toString()));
        return beanClass;

    }

    /**
     * @param className
     * @return
     */
    private Class<?> resolveClass(String className) throws ClassNotFoundException {
        Class<?> beanClass;
        try {
            beanClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            Log.error(e, "不存在[{}]相应的Class", className);
            throw e;
        }
        return beanClass;
    }

    /**
     * 构建{@link RocketListener}定义形式的Consumer的Bean的名称
     * @param factoryMethodName
     * @return
     */
    private String buildRocketConsumerBeanName(String factoryMethodName) {
        return factoryMethodName + Constant.ROCKETMQ_CONSUMER_BEAN_NAME_SUFFIX;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

}
