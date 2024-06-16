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

import java.util.Arrays;

/**
 * @author YXH_XianYu
 * @date 2024/6/16 22:43
 **/
@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Around("execution(* com.yxhxianyu.tiktok.controller.*.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        // 记录请求信息
        String methodName = joinPoint.getSignature().getName();
        String arguments = getArguments(joinPoint.getArgs());
        if (methodName.equals("login") || methodName.equals("register")) {
            arguments = arguments.replaceAll("\"password\":\".*?\"", "\"password\":\"***\"");
        }
        logger.info("Entering method: \"{}\" with arguments: \"{}\"", methodName, arguments);

        Object result;
        try {
            // 执行目标方法
            result = joinPoint.proceed();
        } catch (Throwable e) {
            // 记录异常信息
            logger.error("Exception in method: \"{}\"", methodName, e);
            throw e;
        }

        // 记录响应信息
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
}
