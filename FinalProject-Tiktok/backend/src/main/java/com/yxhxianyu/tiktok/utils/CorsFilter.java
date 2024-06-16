package com.yxhxianyu.tiktok.utils;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 处理跨域请求
 * @author YXH_XianYu
 **/
@Component
public class CorsFilter implements Filter {
    /**
     * 跨域请求Filter
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;

//        httpResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:5000"); // 允许的前端域名
        httpResponse.setHeader("Access-Control-Allow-Origin", "*"); // 允许所有域访问
        httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS"); // 允许的请求方法
        httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, X-Auth-Token, Origin, Authorization"); // 允许的请求头
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true"); // 允许携带cookie

        // 处理预检请求
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        chain.doFilter(request, response);
    }
}
