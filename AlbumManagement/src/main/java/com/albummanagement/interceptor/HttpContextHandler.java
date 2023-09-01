package com.albummanagement.interceptor;

import com.albummanagement.utils.HttpContextUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 该拦截器是用来将http请求的request和response添加到HttpContextUtils的ThreadLocal中
 * 并在执行完毕后删除对应ThreadLocal
 * @Author:gj
 * @DateTime:2023/4/7 8:58
 **/
@Component
public class HttpContextHandler implements HandlerInterceptor {
    /*
        进入Controller方法之前执行可以在此处做一些操作，例如验证用户登录状态，验证权限等。
        如果在 preHandle() 中发现用户没有权限或者未登录，可以直接返回false中断请求，这样就不会进入Controller方法了。
     */
    //将http请求的request和response添加到HttpContextUtils的ThreadLocal中
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Class<?> controllerClass = handlerMethod.getBeanType();
            //getAnnotation()方法用于获取当前 Bean 对象所对应的类上指定类型的注解，返回的是指定类型的注解实例。
            // 判断拦截的是否为Controller
            if (controllerClass.getAnnotation(RestController.class) != null ||
                    controllerClass.getAnnotation(Controller.class) != null) {
                //给ThreadLocal赋值
                HttpContextUtils.setRequest(request);
                HttpContextUtils.setResponse(response);
                return true;
            }
        }
        // 拦截的是静态资源，直接放行
        return true;
    }

    /*
        在视图渲染完成后进行一些操作，例如资源清理、统计响应时间等，那么应该在 afterCompletion() 方法中实现。
        afterCompletion() 方法会在视图渲染完成之后执行，可以在此处做一些操作，例如资源清理、统计响应时间等。
     */
    //执行完毕后，将HttpContextUtils的ThreadLocal中的值清除，避免内存泄露
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //移除ThreadLocal中的值
        HttpContextUtils.remove();
    }
    /*
        在Controller方法执行完成之后进行一些操作，例如记录日志、处理异常等，那么应该在 postHandle() 方法中实现。
        postHandle() 方法会在Controller方法执行完成后，视图渲染之前执行，
        可以在此处做一些操作，例如记录日志、添加一些自定义的信息到请求的attribute中等。
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }
}
