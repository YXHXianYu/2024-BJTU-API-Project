package com.yxhxianyu.tiktok.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author YXH_XianYu
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "video")
public class VideoPojo {

    @TableId(value = "uuid", type = IdType.ASSIGN_UUID)
    private String uuid;

    @TableField(value = "title")
    private String title;

    @TableField(value = "filepath")
    private String filepath;

    @TableField(value = "likes")
    private Integer likes;

    @TableField(value = "userUuid")
    private String userUuid;
}
