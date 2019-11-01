package com.liuzi.rocketmq.util;

/**
 * RocketMQ定义时的常量
 *
 * @author wb-jjb318191
 * @create 2018-03-20 14:38
 */
public class Constant {

    /**
     * 消费者组
     */
    public static final String CONSUMER_GROUP = "consumerGroup";

    /**
     * 主题
     */
    public static final String TOPIC = "topic";

    /**
     * 小标题
     */
    public static final String TAG = "tag";

    /**
     *
     */
    public static final String NAMESRV_ADDR = "namesrvAddr";

    /**
     * 消费Bean
     */
    public static final String CONSUME_BEAN_NAME = "consumeBeanName";

    /**
     * 消费方法
     */
    public static final String CONSUME_METHOD = "consumeMethod";

    /**
     * 消费偏移模式
     */
    public static final String CONSUME_FROM_WHERE = "consumeFromWhere";

    /**
     * RocketMQ消费者Bean名称后缀
     */
    public static String ROCKETMQ_CONSUMER_BEAN_NAME_SUFFIX;//RocketConsumer
    
	public static String _TOPIC;
}
