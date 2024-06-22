package com.yxhxianyu.tiktok.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "autho")
public class AuthoPojo {

    @TableId(value = "uuid")
    private String uuid;

    @TableField(value = "password")
    private String password;

}
