package com.kl.wechat.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface WechatService {
    ResponseEntity<String> wechatHeartbeat(String signature, String timestamp, String nonce, String echostr);
    ResponseEntity<String> wechatDistributor(String requestBody);
    String wechatCommandDistributor(String fromUser, Map<String, Object> tokens, String userRole);
}