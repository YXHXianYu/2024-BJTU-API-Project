package com.kl.wechat.controller;

import com.kl.wechat.service.WechatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wechat")
public class WechatController {

    @Autowired
    private WechatService wechatService;

    @GetMapping
    public ResponseEntity<String> wechatHeartbeat(
            @RequestParam("signature") String signature,
            @RequestParam("timestamp") String timestamp,
            @RequestParam("nonce") String nonce,
            @RequestParam("echostr") String echostr) {
        return wechatService.wechatHeartbeat(signature, timestamp, nonce, echostr);
    }

    @PostMapping
    public ResponseEntity<String> wechatDistributor(@RequestBody String requestBody) {
        return wechatService.wechatDistributor(requestBody);
    }

}
