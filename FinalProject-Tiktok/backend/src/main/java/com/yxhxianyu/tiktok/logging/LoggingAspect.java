package com.yxhxianyu.tiktok.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @author YXH_XianYu
 **/
@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Around("execution(* com.yxhxianyu.tiktok.controller.*.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String clientIp = getClientIp(request);

        String methodName = joinPoint.getSignature().getName();
        String arguments = getArguments(joinPoint.getArgs());
        if (methodName.equals("login") || methodName.equals("register")) {
            arguments = arguments.replaceAll("\"password\":\".*?\"", "\"password\":\"***\"");
        }
        logger.info("Entering method: \"{}\" from IP: \"{}\" with arguments: \"{}\"", methodName, clientIp, arguments);

        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            logger.error("Exception in method: \"{}\"", methodName, e);
            throw e;
        }

        long timeTaken = System.currentTimeMillis() - startTime;
        logger.info("Exiting method: \"{}\" with result: \"{}\" in {} ms", methodName, getResult(result), timeTaken);
        return result;
    }

    private String getArguments(Object[] args) {
        try {
            return objectMapper.writeValueAsString(args);
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize arguments", e);
            return Arrays.toString(args);
        }
    }

    private String getResult(Object result) {
        try {
            return objectMapper.writeValueAsString(result);
        } catch (InvalidDefinitionException e) {
//            logger.error("Failed to serialize result", e);
            return "[Cannot serialize result]";
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize result", e);
            return String.valueOf(result);
        }
    }
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
