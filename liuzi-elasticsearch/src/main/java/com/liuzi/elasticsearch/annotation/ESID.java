package com.liuzi.elasticsearch.annotation;

import java.lang.annotation.*;

/**
 * @description: ES entity 标识ID的注解,在es entity field上添加
 * @author: zsy
 * @create: 2019-01-18 16:09
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface ESID {
	
}
