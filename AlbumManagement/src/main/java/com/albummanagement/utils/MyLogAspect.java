package com.albummanagement.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 本类用来对添加了@Log注解的方法进行代理
 * 为其添加日志功能
 * @Author:gj
 * @DateTime:2023/4/3 15:54
 **/
@Component
@Aspect//将本类声明为一个切面组件
@Slf4j//日志类
public class MyLogAspect {
    //公共切入点提取，对所有添加了com.albummanagement.utils.Log注解的方法进行代理
    @Pointcut("@annotation(com.albummanagement.utils.MyLog)")
    public void logPointCut(){}

    //环绕通知
    /**
    * 日志将记录方法执行时间
    * 注解上的信息
    * 方法名
    * 方法参数
    * 客户端相关信息
    * 方法执行耗费时间
    * */
    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();//获取当前时间
        Object result = point.proceed();//执行被代理的方法
        long time = System.currentTimeMillis() - beginTime;//获取执行的时间
        recordLog(point,time);
        return result;
    }
    /**
     * 这个方法用来真正记录日志
     * */
    private void recordLog(ProceedingJoinPoint point, long time){
        //MethodSignature是Spring AOP中的一个接口，它用于表示方法签名，即一个方法的声明，包括方法名、参数类型以及返回值类型等信息
        MethodSignature signature = (MethodSignature) point.getSignature();
        //在AOP编程中，通常需要获取目标方法的Method对象，以便进行一些其它操作，例如调用方法的注解信息、获取方法的返回值、抛出异常等。
        Method method = signature.getMethod();//getMethod()用于获取目标方法的java.lang.reflect.Method对象。
        //这就是自定义的日志注解，getAnnotation()是Java反射机制提供的一个方法，
        //用于获取方法上指定类型的注解。该方法的参数是一个Class对象，表示需要获取的注解类型。如果该方法上没有指定类型的注解，则返回null。
        MyLog myLog = method.getAnnotation(MyLog.class);
        log.info("================================log start================================");
        //添加时间
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
        log.info("时间:{}",sdf.format(date));

        //得到注解上的模块名和操作名
        log.info("模块:{}", myLog.moduleName());
        log.info("操作:{}", myLog.operationName());

        //请求的方法名
        String className = point.getTarget().getClass().getName();
        String methodName = signature.getName();
        log.info("请求方法名:{}", className + "." + methodName + "()");

        //方法参数
        Object[] args = point.getArgs();
        if (args.length != 0) {
            String params = JSON.toJSONString(args[0]);
            log.info("参数:{}", params);
        }else{
            log.info("参数:{}", "null");
        }

        //客户端相关信息
        log.info("请求地址:{}",HttpContextUtils.getURL());
        log.info("Ip地址:{}",HttpContextUtils.getIP());

        //花费时间
        log.info("用时: {} ms", time);
        log.info("================================log end================================");
    }
}
