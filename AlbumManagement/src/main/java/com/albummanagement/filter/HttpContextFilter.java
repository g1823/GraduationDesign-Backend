package com.albummanagement.filter;

import com.albummanagement.utils.HttpContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 本类为一个过滤器
 * 该类用于在每次请求进来时，
 * 将当前线程的 HttpServletRequest 和 HttpServletResponse 对象设置到 ThreadLocal 对象中
 * 然后在请求结束时，清除这些对象。
 * @Author:gj
 * @DateTime:2023/4/3 16:43
 **/
public class HttpContextFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            HttpContextUtils.setRequest(request);
            HttpContextUtils.setResponse(response);
            filterChain.doFilter(request, response);
        } finally {
            HttpContextUtils.remove();
        }
    }
}
