package com.liuzi.activemq.consumer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.jms.MessageListener;


@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Consumer {
	
	String[] name() default {};
	
	boolean queue() default true;
	
	Class<? extends MessageListener> clazz() default OnMessage.class;
}
