package com.albummanagement.config;

import com.albummanagement.interceptor.HttpContextHandler;
import com.albummanagement.interceptor.LoginHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web相关的配置类
 * 用来配置过滤器、拦截器等
 *
 * @Author:gj
 * @DateTime:2023/4/3 16:35
 **/
@Configuration
public class WebMVCConfig implements WebMvcConfigurer {
    @Autowired
    HttpContextHandler httpContextHandler;
    @Autowired
    LoginHandler loginHandler;

    /*
     * CorsRegistry类是用于配置跨域资源共享（CORS）的对象之一。
     * CORS是一种Web浏览器的安全机制，用于限制在一个域中运行的Web应用程序对来自不同源的资源的访问。
     *   addMapping()方法指定哪些路径可以跨域访问
     *   allowedOrigins()指定允许跨域访问的源
     *   allowedMethods()指定允许使用的HTTP方法
     *   allowedHeaders()指定允许使用的请求头
     *   exposedHeaders()指定哪些响应头应该暴露给客户端。
     * */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //跨域配置，允许9999访问
        registry.addMapping("/**").allowedOrigins("http://localhost:9999");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
         /*
            addInterceptor表示将自定义的拦截器添加到拦截器链中
            addPathPatterns表示需要拦截的请求url
            excludePathPatterns表示放行的请求url
         */

        //登录拦截器
        registry.addInterceptor(loginHandler)
                .addPathPatterns("/test")
                .addPathPatterns("/pictures")
                .addPathPatterns("/pictures/**")
                .addPathPatterns("/pictures/getCategoryPics")
                .addPathPatterns("/category")
                .addPathPatterns("/category/delete")
                .addPathPatterns("/user/**")
                .addPathPatterns("/user/changeAvatar")
                .addPathPatterns("/user/changePassword")
                .addPathPatterns("/comment")
                .addPathPatterns("/like/**")
                .addPathPatterns("/friend/**");

        //http请求拦截器
        registry.addInterceptor(httpContextHandler)
                .addPathPatterns("/register")
                .addPathPatterns("/login")
                .excludePathPatterns("/test");

    }

    //注册HttpContextFilter拦截器
//    @Bean
//    public FilterRegistrationBean<HttpContextFilter> openHttpContextFilter(){
//        FilterRegistrationBean<HttpContextFilter> registration = new FilterRegistrationBean<>();//这个类来注册过滤器
//        registration.setFilter(new HttpContextFilter());//设置要注册的过滤器对象
//        registration.addUrlPatterns("/*");//用于设置过滤器的URL模式
//        registration.setName("HttpContextFilter");//用于指定Filter的名称
//        registration.setEnabled(true);//用于设置过滤器是否启用
//        registration.setOrder(1);//用于设置过滤器的执行顺序
//        return registration;
//    }
}
