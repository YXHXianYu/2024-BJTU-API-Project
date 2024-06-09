package com.yxhxianyu.tiktok.controller;

import com.yxhxianyu.tiktok.pojo.*;
import com.yxhxianyu.tiktok.service.UserService;
import com.yxhxianyu.tiktok.utils.Result;
import com.yxhxianyu.tiktok.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author YXH_XianYu
 **/
@RestController
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 注册学生用户
     */
    @RequestMapping(value = "/api/v1/users", method = RequestMethod.POST)
    public String register(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam("email") String email,
                           @RequestParam("telephone") String telephone) {
        String encodedPassword = Util.passwordEncoder(password);

        Result<String> user = userService.createUser(username, encodedPassword, email, telephone);
        if (user.err != null) {
            return Util.getResponse(422, user.err);
        }
        System.out.println("注册：" + username + " 成功");
        return Util.getOkResponse("注册成功");
    }

    /**
     * 登录用户
     */
    @RequestMapping(value = "/api/v1/login", method = RequestMethod.POST)
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password) {
        String encodedPassword = Util.passwordEncoder(password);

        Result<UserPojo> user = userService.getUserByName(username);

        if(user.err != null) {
            System.out.println("登录：" + username + " 用户不存在");
            return Util.getResponse(404, "用户不存在");
        } else if(!user.val.getPassword().equals(encodedPassword)) {
            System.out.println("登录：" + username + " 密码错误");
            return Util.getResponse(401, "密码错误");
        } else {
            String token = Util.tokenEncoder(username, password);
            System.out.println("登录：" + username + " 登录成功");
            return Util.getOkResponse(
                    "登陆成功，请使用data.token中的身份验证",
                    new HashMap<String, String>() {{
                        put("token", token);
                    }}
            );
        }
    }

    /**
     * 获取所有用户信息
     */
    @RequestMapping(value = "/api/user/get_all", method = RequestMethod.POST)
    public String getAllUsers(@RequestParam("token") String token) {
        Result<UserPojo> user = userService.getUserByToken(token);
        if(user.err != null) { return Util.getResponse(401, "用户未登录"); }

        return Util.getOkResponse("获取成功", userService.getAllUsers());
    }

}
