package com.kl.wechat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("offers")
public class Offer {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String company;

    private String city;

    private String position;

    private Integer salary;

    private Date datetime;

    @TableField("from_user")
    private Long fromUser;

    @Override
    public String toString() {
        return "Offer (" + company + ", " + city + ", " + position + ", " + salary + ")";
    }
}
