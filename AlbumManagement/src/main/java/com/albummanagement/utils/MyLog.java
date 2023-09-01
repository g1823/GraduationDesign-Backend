package com.albummanagement.utils;

import java.lang.annotation.*;

/**
 * 日志注解，对添加了该注解的方法将进行代理
 * 提供日志处理
 * @Author:gj
 * @DateTime:2023/4/3 15:47
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyLog {
    String moduleName() default "模块名为空";
    String operationName() default "操作为空";
}
