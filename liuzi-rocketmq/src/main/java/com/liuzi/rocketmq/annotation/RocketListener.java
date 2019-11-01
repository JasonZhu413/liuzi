package com.liuzi.rocketmq.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.rocketmq.common.consumer.ConsumeFromWhere;

/**
 * RocketMQ消费者监听
 * @author zsy
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface RocketListener {

    /**
     * 消费者组
     * @return
     */
    String consumerGroup();

    /**
     * @return
     */
    String topic() default "";

    /**
     * @return
     */
    String tag() default "*";

    /**
     * @return
     */
    String namesrvAddr() default "";

    /**
     * 从哪里开始消费
     * @return
     */
    ConsumeFromWhere consumeFromWhere() default ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET;

}
