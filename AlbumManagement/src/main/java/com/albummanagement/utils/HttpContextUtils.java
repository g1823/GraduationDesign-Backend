package com.albummanagement.utils;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 从本类中可以获取到当前线程中的
 * HttpServletRequest
 * HttpServletResponse
 * 以及与其相关的各种对象
 *
 * @Author:gj
 * @DateTime:2023/4/3 16:19
 **/
public class HttpContextUtils {
    //下面两个ThreadLocal分别存储请求和响应
    private static final ThreadLocal<HttpServletRequest> requestThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<HttpServletResponse> responseThreadLocal = new ThreadLocal<>();

    public static void setRequest(HttpServletRequest request) {
        requestThreadLocal.set(request);
    }

    public static void setResponse(HttpServletResponse response) {
        responseThreadLocal.set(response);
    }


    public static void remove() {
        requestThreadLocal.remove();
        responseThreadLocal.remove();
    }

    public static String getIP() {
        return requestThreadLocal.get().getRemoteAddr();
    }

    public static StringBuffer getURL() {
        return requestThreadLocal.get().getRequestURL();
    }

}
