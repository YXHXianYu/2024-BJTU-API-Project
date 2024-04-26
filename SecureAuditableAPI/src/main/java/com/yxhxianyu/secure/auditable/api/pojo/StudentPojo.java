package com.yxhxianyu.secure.auditable.api.pojo;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

/**
 * Pojo类只储存信息，不做任何其他操作（如构造函数等）
 * 所有操作应该在DAO中完成
 * @author YXH_XianYu
 * @date 2024/4/26 10:28
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "student")
public class StudentPojo {

    @TableId(value = "uuid", type = IdType.ASSIGN_UUID)
    private String uuid;

    @TableField(value = "name")
    private String name;

    @TableField(value = "gender")
    private Integer gender;

    @TableField(value = "age")
    private Integer age;

    public String toString() {
        return JSONObject.toJSONString(
                new HashMap<String, Object>() {{
                    put("uuid", uuid);
                    put("name", name);
                    put("gender", gender);
                    put("age", age);
                }}
        );
    }

    public HashMap<String, Object> toHashMap() {
        return new HashMap<>() {{
            put("uuid", uuid);
            put("name", name);
            put("gender", gender);
            put("age", age);
        }};
    }
}
