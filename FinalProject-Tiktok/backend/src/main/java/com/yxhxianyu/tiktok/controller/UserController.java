package com.yxhxianyu.tiktok.controller;

import com.yxhxianyu.tiktok.pojo.*;
import com.yxhxianyu.tiktok.service.UserService;
import com.yxhxianyu.tiktok.utils.Result;
import com.yxhxianyu.tiktok.utils.Util;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yxhxianyu.tiktok.utils.Util.checkPermission;

/**
 * @author YXH_XianYu
 **/
@RestController
public class UserController {

    @Autowired
    UserService userService;

    public static class RegisterRequest {
        public String username;
        public String password;
        public String email;
        public String telephone;
    }

    /**
     * 注册学生用户
     */
    @RequestMapping(value = "/api/v1/users", method = RequestMethod.POST)
    public ResponseEntity<Object> register(@RequestBody RegisterRequest req) {
        String encodedPassword = Util.passwordEncoder(req.password);

        Result<String> user = userService.createUser(req.username, encodedPassword, req.email, req.telephone);

        if (user.err != null) {
            return Util.getResponse(422, user.err);
        }
        System.out.println("注册：" + req.username + " 成功");
        return Util.getOkResponse("注册成功");
    }

    public static class LoginRequest {
        public String username;
        public String password;
    }

    /**
     * 登录用户
     */
    @CrossOrigin
    @RequestMapping(value = "/api/v1/sessions", method = RequestMethod.POST)
    public ResponseEntity<Object> login(@RequestBody LoginRequest req) {
        String encodedPassword = Util.passwordEncoder(req.password);

        Result<UserPojo> user = userService.getUserByName(req.username);
        if(user.err != null) {
            System.out.println("登录：" + req.username + " 用户不存在");
            return Util.getResponse(404, "用户不存在");
        }

        Result<String> passwd = userService.getUserPasswordByUUID(user.val.getUuid());

        if(passwd.err != null) {
            System.out.println("登录：" + req.username + " 鉴权服务器出错！");
            return Util.getResponse(400, "鉴权服务器出错");
        } else if(!passwd.val.equals(encodedPassword)) {
            System.out.println("登录：" + req.username + " 密码错误");
            return Util.getResponse(401, "密码错误");
        } else {
            String token = Util.tokenEncoder(req.username, req.password);
            System.out.println("登录：" + req.username + " 登录成功");
            return Util.getOkResponse("登陆成功，请使用data.token中的身份验证", new HashMap<String, Object>() {{
                put("token", token);
            }});
        }
    }

    /**
     * 获取所有用户信息
     */
    @RequestMapping(value = "/api/v1/users", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllUsers(@RequestHeader String Authorization) {
        Result<UserPojo> user = userService.getUserByToken(Authorization);
        if (user.err != null) { return Util.getResponse(401, "用户未登录"); }

        if (!checkPermission(user.val)) { return Util.getResponse(403, "权限不足"); }

        List<UserPojo> users = userService.getAllUsers().val;
        for (UserPojo u : users) {
            u.setPassword(null);
        }
        return Util.getOkResponse("获取成功", users);
    }

    /**
     * 获取用户信息
     */
    @RequestMapping(value = "/api/v1/users/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<Object> getUser(@PathVariable String uuid, @RequestHeader String Authorization) {
        Result<UserPojo> user = userService.getUserByToken(Authorization);
        if (user.err != null) { return Util.getResponse(401, "用户未登录"); }

        if (!checkPermission(user.val) && !user.val.getUuid().equals(uuid)) { return Util.getResponse(403, "权限不足"); }

        Result<UserPojo> u = userService.getUserByUUID(uuid);
        if (u.err != null) {
            return Util.getResponse(404, "用户不存在");
        }
        u.val.setPassword(null);
        return Util.getOkResponse("获取成功", u.val);
    }


}
