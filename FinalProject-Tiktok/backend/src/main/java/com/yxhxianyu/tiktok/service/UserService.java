package com.yxhxianyu.tiktok.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yxhxianyu.tiktok.dao.UserDao;
import com.yxhxianyu.tiktok.pojo.UserPojo;
import com.yxhxianyu.tiktok.utils.Result;
import com.yxhxianyu.tiktok.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * User表的服务
 * @author YXH_XianYu
 * @date 2023/10/7 23:28
 **/
@Service
public class UserService {

    @Autowired
    UserDao userDao;

    /**
     * 插入一条新的用户
     * 并返回该用户的UUID
     */
    public Result<String> createUser(String username, String password, String email, String telephone) {
        String uuid = UUID.randomUUID().toString();
        try {
            userDao.insert(new UserPojo(uuid, username, password, email, telephone));
            return new Result<>(null, uuid);
        } catch (DuplicateKeyException e) {
            System.out.println("Insert failed: duplicate username");
            return new Result<>("ERROR: 用户名重复", null);
        } catch (DataIntegrityViolationException e) {
            System.out.println("Insert failed: data integrity violation (maybe data is too long)");
            return new Result<>("ERROR: 数据不合法 (可能因为数据过长)", null);
        }
    }

    /**
     * 根据UUID查询一条用户信息
     */
    public Result<UserPojo> getUserByUUID(String uuid) {
        UserPojo user = userDao.selectById(uuid);
        if (user == null) {
            return new Result<>("Not Found", null);
        } else {
            return new Result<>(null, user);
        }
    }

    /**
     * 根据名字查询一条用户信息
     */
    public Result<UserPojo> getUserByName(String username) {
        UserPojo user = userDao.selectOne(new QueryWrapper<UserPojo>().eq("username", username));
        if (user == null) {
            return new Result<>("Not Found", null);
        } else {
            return new Result<>(null, user);
        }
    }

    /**
     * 根据token查询一条用户信息
     */
    public Result<UserPojo> getUserByToken(String token) {
        try {
            return getUserByName(Util.tokenDecoder(token));
        } catch (IllegalArgumentException e) {
            return new Result<>("IllegalArgument: token is invalid", null);
        }
    }

    /**
     * 根据名字获取UUID
     */
    public Result<String> getUUIDByName(String name) {
        UserPojo pojo = userDao.selectOne(new QueryWrapper<UserPojo>().eq("username", name));
        if (pojo == null) {
            return new Result<>("Not Found", null);
        } else {
            return new Result<>(null, pojo.getUuid());
        }
    }

    /**
     * 查询所有用户
     */
    public Result<List<UserPojo>> getAllUsers() {
        List<UserPojo> users = userDao.selectList(new QueryWrapper<>());
        if (users.isEmpty()) {
            return new Result<>("Not Found", null);
        } else {
            return new Result<>(null, users);
        }
    }

    /**
     * 根据UUID删除一条用户
     * 在得到UUID时，请注意你的UserPojo对象非空，否则会在函数外部产生NullPointerException
     */
    public void deleteUserByUUID(String uuid) {
        userDao.deleteById(uuid);
    }

    /**
     * 根据名称删除一条用户
     */
    public void deleteUserByName(String username) {
        Result<UserPojo> user = getUserByName(username);
        if(user.err != null)
            userDao.deleteById(user.val.getUuid());
    }
}
