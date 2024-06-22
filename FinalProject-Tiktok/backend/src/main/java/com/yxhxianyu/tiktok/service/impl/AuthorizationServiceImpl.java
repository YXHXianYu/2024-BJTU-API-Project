package com.yxhxianyu.tiktok.service.impl;

import com.yxhxianyu.tiktok.client.TTSocket;
import com.yxhxianyu.tiktok.client.ThriftClientConnectPoolFactory;
import com.yxhxianyu.tiktok.dto.AuthorizationServiceDTO;
import com.yxhxianyu.tiktok.pojo.AuthorizationPOJO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AuthorizationServiceImpl implements AuthorizationServiceDTO.Iface {

    @Autowired
    ThriftClientConnectPoolFactory thriftPool;

    //根据名称获取Demo
    @Override
    public AuthorizationPOJO getPasswordByName(String uuid) {
        TTSocket ttSocket = null;
        try {
            //通过对象池获得一个客户端链接
            ttSocket = thriftPool.getConnect();
            return ttSocket.getService().getPasswordByName(uuid);
        } catch (Exception e) {
            e.printStackTrace();
            //出现异常从连接器移出
            thriftPool.invalidateObject(ttSocket);
            ttSocket = null;
        } finally {
            if (ttSocket != null) {
                //归还链接
                thriftPool.returnConnection(ttSocket);
            }
        }
        return null;
    }

    //保存Demo信息
    @Override
    public void save(AuthorizationPOJO demoPOJO) {
        TTSocket ttSocket = null;
        try {
            //通过对象池获得一个客户端链接
            ttSocket = thriftPool.getConnect();
            ttSocket.getService().save(demoPOJO);
        } catch (Exception e) {
            e.printStackTrace();
            //出现异常从连接器移出
            thriftPool.invalidateObject(ttSocket);
            ttSocket = null;
        } finally {
            if (ttSocket != null) {
                //归还链接
                thriftPool.returnConnection(ttSocket);
            }
        }
    }
}
