package com.kl.wechat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kl.wechat.entity.User;
import com.kl.wechat.mapper.UserMapper;
import com.kl.wechat.service.UserService;
import com.kl.wechat.utils.Constants;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.kl.wechat.utils.Constants.*;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final Map<String, User> userMap = new HashMap<>();

    @Override
    public User getUserByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return getOne(queryWrapper);
    }

    @Override
    public void createUser(String username) {
        User user = new User();
        user.setUsername(username);
        save(user);
    }

    @Override
    public void updateUser(User user) {
        updateById(user);
    }

    @Override
    public int checkUserState(String username, String command, boolean createIfNotExist) {
        User user = getUserByUsername(username);
        if (user == null) {
            if (createIfNotExist) {
                createUser(username);
                user = getUserByUsername(username);
            } else {
                return Constants.AUTHORITY_CHECK_USER_NOT_EXIST;
            }
        }

        int state = user.getState();
        if (state == Constants.USER_STATE_BANNED) {
            return Constants.AUTHORITY_CHECK_FAILED;
        }

        // 根据命令类型和用户状态进行权限检查
        switch (command) {
            case "ban":
            case "unban":
                // 只有管理员可以执行这些命令
                if (state != Constants.USER_STATE_ADMIN) {
                    return Constants.AUTHORITY_CHECK_FAILED;
                }
                break;
            // 添加其他命令的权限检查逻辑...
            default:
                break;
        }

        return Constants.AUTHORITY_CHECK_PASS;
    }

    @Override
    public void banUser(String username) {
        User user = getUserByUsername(username);
        if (user != null) {
            user.setState(USER_STATE_BANNED);
            updateUser(user);
        }
    }

    @Override
    public void unbanUser(String username) {
        User user = getUserByUsername(username);
        if (user != null) {
            user.setState(USER_STATE_DEFAULT);
            updateUser(user);
        }
    }
}