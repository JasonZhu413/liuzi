package com.liuzi.rocketmq.bean;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PreDestroy;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;

import com.liuzi.util.common.Log;


/**
 * RocketMQ Consumer生命周期处理器
 * @author zsy
 */
public class RocketConsumerLifecycleProcessor implements SmartLifecycle {

    @Autowired
    private Map<String, DefaultMQPushConsumer> rocketMQConsumers;

    private volatile boolean isRunning = false;

    @Override
    public void start() {
        for (Entry<String, DefaultMQPushConsumer> entry : rocketMQConsumers.entrySet()) {
            try {
                entry.getValue().start();
            } catch (MQClientException e) {
                Log.error(e, "启动[{}]消费者失败: ", entry.getKey());
            }
        }
        isRunning = true;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }

    @PreDestroy
    public void destroy() {
        isRunning = false;
        for (DefaultMQPushConsumer consumer : rocketMQConsumers.values()) {
            consumer.shutdown();
        }
    }

    @Override
    public void stop(Runnable runnable) {

    }

    @Override
    public void stop() {

    }

}
