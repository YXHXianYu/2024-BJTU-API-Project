package com.yxhxianyu.tiktok.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yxhxianyu.tiktok.dao.AuthoDao;
import com.yxhxianyu.tiktok.dto.AuthorizationServiceDTO;
import com.yxhxianyu.tiktok.pojo.AuthoPojo;
import com.yxhxianyu.tiktok.pojo.AuthorizationPOJO;
import com.yxhxianyu.tiktok.pojo.ExceptionPOJO;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationServiceImpl implements AuthorizationServiceDTO.Iface {

    @Autowired
    AuthoDao authoDao;

    @Override
    public AuthorizationPOJO getPasswordByName(String uuid) throws ExceptionPOJO, TException {
        AuthorizationPOJO pojo = new AuthorizationPOJO();
        AuthoPojo autho = authoDao.selectOne(new QueryWrapper<AuthoPojo>().eq("uuid", uuid));
        if (autho == null) {
            ExceptionPOJO exceptionPOJO = new ExceptionPOJO();
            exceptionPOJO.setCode(400);
            exceptionPOJO.setMsg("Thrift-Server 返回失败，错误原因：用户不存在");
            System.out.println("Thrift-Server 返回失败，错误原因：用户不存在");
            throw exceptionPOJO;
        }
        System.out.println("Thrift-Server 返回成功");
        pojo.setUuid(autho.getUuid());
        pojo.setPassword(autho.getPassword());
        return pojo;
    }

    @Override
    public void save(AuthorizationPOJO data) throws ExceptionPOJO, TException {
        AuthoPojo autho = new AuthoPojo();
        autho.setUuid(data.getUuid());
        autho.setPassword(data.getPassword());
        try {
            authoDao.insert(autho);
        } catch (Exception e) {
            ExceptionPOJO pojo = new ExceptionPOJO();
            pojo.setCode(400);
            pojo.setMsg("Thrift-Server 保存失败，错误原因：" + e.getMessage());
            System.out.println("Thrift-Server 保存失败，错误原因：" + e.getMessage());
            throw pojo;
        }
        System.out.println("Thrift-Server 保存成功");
    }
}
