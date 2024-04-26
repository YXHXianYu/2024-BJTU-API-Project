package com.yxhxianyu.secure.auditable.api.utils;

/**
 * @author YXH_XianYu
 * @date 2024/4/26 10:23
 **/

import com.alibaba.fastjson2.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author YXH_XianYu
 * @date 2024/4/26 10:28
 **/
public class Utils {

    /* ----- ----- Http请求 ----- ----- */

    /**
     * 生成一个Http请求的Response
     * @param code 状态码
     * @param message 信息内容
     * @param data 数据
     * @return 字符串形式的Response
     */
    public static String getResponse(int code, String message, Object data) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("message", message);
        if(data != null)
            map.put("data", data);
        return JSONObject.toJSONString(map);
    }

    /**
     * 生成一个Http请求的Response
     * @param code 状态码
     * @param message 信息内容
     * @return 字符串形式的Response
     */
    public static String getResponse(int code, String message) {
        return getResponse(code, message, null);
    }

    /**
     * 生成一个状态码为200的Http请求的Response
     * @param message 信息内容
     * @return 字符串形式的Response
     */
    public static String getOkResponse(String message) {
        return getResponse(200, message);
    }

    /**
     * 生成一个状态码为200的Http请求的Response
     * @param message 信息内容
     * @param data 数据
     * @return 字符串形式的Response
     */
    public static String getOkResponse(String message, Object data) {
        return getResponse(200, message, data);
    }

}