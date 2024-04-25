package com.kl.wechat.service;

import com.kl.wechat.entity.User;

public interface UserService {
    User getUserByUsername(String username);
    void createUser(String username);
    void updateUser(User user);
    int checkUserState(String username, String command, boolean createIfNotExist);
    void banUser(String username);
    void unbanUser(String username);
}