package com.yxhxianyu.tiktok.service;

import com.yxhxianyu.tiktok.pojo.AuthorizationPOJO;
import org.springframework.stereotype.Service;

@Service
public interface AuthorizationService {

    //根据名称获取Demo
    AuthorizationPOJO getPasswordByUuid(String uuid);

    //保存Demo信息
    void save(AuthorizationPOJO demoPOJO);

}
