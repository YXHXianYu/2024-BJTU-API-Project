package com.kl.wechat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import static com.kl.wechat.utils.Constants.USER_STATE_DEFAULT;

@Data
@TableName("users")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private Integer state;

    @TableField("request_queue")
    private String requestQueue;

    public User() {
        this.state = USER_STATE_DEFAULT;
        this.requestQueue = "[]";
    }

    @Override
    public String toString() {
        return "User (" + username + ", " + state + ")";
    }
}
