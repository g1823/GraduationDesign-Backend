package com.albummanagement.interceptor;

import com.albummanagement.utils.HttpContextUtils;
import com.albummanagement.utils.MyException;
import com.albummanagement.utils.RedisUtils;
import com.albummanagement.utils.UserThreadLocal;
import com.albummanagement.view.vo.UserVo;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截用户是否登录
 *
 * @Author:gj
 * @DateTime:2023/4/13 21:06
 **/
@Component
public class LoginHandler implements HandlerInterceptor {
    //判断用户是否已经登录
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Class<?> controllerClass = handlerMethod.getBeanType();
            //getAnnotation()方法用于获取当前 Bean 对象所对应的类上指定类型的注解，返回的是指定类型的注解实例。
            // 判断是否为 CORS 预检请求
            if ("OPTIONS".equals(request.getMethod())) {
                response.setStatus(HttpServletResponse.SC_OK);
                return true;
            }
            // 判断拦截的是否为Controller
            if (controllerClass.getAnnotation(RestController.class) != null ||
                    controllerClass.getAnnotation(Controller.class) != null) {
                String token = request.getHeader("Authorization");
                //路径/pictures的post请求
                String url = request.getRequestURI();
                String method = request.getMethod();
                if (url.startsWith("/pictures")) {//路径/category的所有请求
                    if (method.equals("POST")) {
                        setUserThreadLocal(token);
                    }
                    if(url.startsWith("/pictures/getCategoryPics")){
                        setUserThreadLocal(token);
                    }
                    if(url.startsWith("/pictures/search")){
                        setUserThreadLocal(token);
                    }
                } else if (url.startsWith("/category")) {//路径/category的所有请求
                    setUserThreadLocal(token);
                } else if (url.startsWith("/user")) {//路径/user下的所有请求，包括子请求，例如/user/changePassword
                    setUserThreadLocal(token);
                } else if(url.startsWith("/comment")){//路径/comment下的所有请求
                    setUserThreadLocal(token);
                } else if(url.startsWith("/like")){
                    setUserThreadLocal(token);
                } else if(url.startsWith("/friend")){
                    setUserThreadLocal(token);
                }
            }
        }
        // 拦截的是静态资源，直接放行
        return true;
    }

    private boolean setUserThreadLocal(String token) {
        if (token == null) {
            throw new MyException("NOT_LOGIN");
        } else {
            UserVo userByToken = RedisUtils.getUserByToken(token);
            if (userByToken == null) {
                throw new MyException("LOGIN_EXPIRED");
            } else {
                UserThreadLocal.setUserVo(userByToken);
                return true;
            }
        }
    }
}
