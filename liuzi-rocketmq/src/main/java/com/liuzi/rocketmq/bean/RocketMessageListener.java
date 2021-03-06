package com.liuzi.rocketmq.bean;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.List;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;


/**
 * 封装{@link RocketListener}标识的方法成一个消息消费器
 *
 * @author wb-jjb318191
 * @create 2018-03-20 10:03
 */
public class RocketMessageListener implements MessageListenerConcurrently {

    private Object consumeBean;
    private Method consumeMethod;
    private static final String ERROR_MSG_PREFIX = "[@RocketListener]标识的方法";

    public RocketMessageListener(Object consumeBean, Method consumeMethod) {
        this.consumeBean = consumeBean;
        this.consumeMethod = consumeMethod;
        checkConsumeMethod();
    }

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        Class<?>[] types = consumeMethod.getParameterTypes();
        Object[] args = new Object[types.length];
        if (types[0] == List.class) {
            args[0] = msgs;
        } else {
            args[0] = msgs.get(0);
        }
        if (types.length == 2) {
            args[1] = context;
        }
        boolean result = (boolean)ReflectionUtils.invokeMethod(consumeMethod, consumeBean, args);
        return result ? ConsumeConcurrentlyStatus.CONSUME_SUCCESS : ConsumeConcurrentlyStatus.RECONSUME_LATER;
    }

    /**
     * 校验{@link RocketListener}标识的方法正确性
     */
    private void checkConsumeMethod() {
        int paramLength = consumeMethod.getParameterCount();
        Assert.state(paramLength == 1 || paramLength == 2, ERROR_MSG_PREFIX + "参数长度只能在1和2之间");
        Parameter param0 = consumeMethod.getParameters()[0];
        boolean isMsg = param0.getType().isAssignableFrom(MessageExt.class);
        boolean isList = param0.getType().isAssignableFrom(List.class);
        Assert.state(isMsg || isList, ERROR_MSG_PREFIX + "第一个参数只能是MessageExt或List<MessageExt>类型");
        if (isList) {
            Class<?> generic = ResolvableType.forMethodParameter(consumeMethod, 0).resolveGeneric(0);
            Assert.isAssignable(MessageExt.class, generic, ERROR_MSG_PREFIX + "第一个参数的泛型只能是MessageExt类型");
        }
        if (paramLength == 2) {
            Assert.state(consumeMethod.getParameters()[1].getType() == ConsumeConcurrentlyContext.class,
                ERROR_MSG_PREFIX + "第2个参数类型只能是[ConsumeConcurrentlyContext]");
        }
        Assert.state(Modifier.isPublic(consumeMethod.getModifiers()), ERROR_MSG_PREFIX + "修饰符必须为[Public]");
        Assert.state(consumeMethod.getReturnType() == boolean.class, ERROR_MSG_PREFIX + "返回值类型必须为[boolean]");
    }

}