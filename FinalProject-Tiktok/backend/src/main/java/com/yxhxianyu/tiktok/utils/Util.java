package com.yxhxianyu.tiktok.utils;

import com.yxhxianyu.tiktok.pojo.UserPojo;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.el.parser.Token;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author YXH_XianYu
 **/
public class Util {

    /* ----- ----- Http请求 ----- ----- */

    /**
     * 生成一个Http请求的Response
     * @param code 状态码
     * @param message 信息内容
     * @param data 数据
     * @return 字符串形式的Response
     */
    public static ResponseEntity<Object> getResponse(int code, String message, Object data) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("message", message);
        if(data != null)
            map.put("data", data);
        return ResponseEntity.status(code).body(map);
    }

    /**
     * 生成一个Http请求的Response
     * @param code 状态码
     * @param message 信息内容
     * @return 字符串形式的Response
     */
    public static ResponseEntity<Object> getResponse(int code, String message) {
        return getResponse(code, message, null);
    }

    /**
     * 生成一个状态码为200的Http请求的Response
     * @param message 信息内容
     * @return 字符串形式的Response
     */
    public static ResponseEntity<Object> getOkResponse(String message) {
        return getResponse(200, message);
    }

    /**
     * 生成一个状态码为200的Http请求的Response
     * @param message 信息内容
     * @param data 数据
     * @return 字符串形式的Response
     */
    public static ResponseEntity<Object> getOkResponse(String message, Object data) {
        return getResponse(200, message, data);
    }

    /* ----- ----- 加密 ----- ----- */

    private static String ENCODER_SALT() {
        return "1145141919810"; // 哼，哼哼，啊啊啊啊啊啊啊啊啊啊啊
    }

    /**
     * 密码加密器
     * @param password 原始密码
     * @return 加密后的密码
     */
    public static String passwordEncoder(String password) {
        return DigestUtils.md5Hex(password + ENCODER_SALT());
    }

    /**
     * Token加密
     * @param username 该用户的用户名
     * @param password 该用户的密码
     * @return token
     */
    @SuppressWarnings("unused")
    public static Result<String> tokenEncoder(String username, String password) {
        return TokenUtils.tokenEncoder(username, password);
    }

    /**
     * Token解密
     * @param token token
     * @return 用户名
     */
    public static Result<String> tokenDecoder(String token) throws IllegalArgumentException {
        return TokenUtils.tokenDecoder(token);
    }

    /* ----- ----- 权限 ----- ----- */

    public static boolean checkPermission(UserPojo user) {
        return user.getUsername().equals("admin");
    }

    /* ----- ----- 视频哈希 ----- ----- */

    public static String calculateHash(InputStream inputStream, String algorithm) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        try (DigestInputStream dis = new DigestInputStream(inputStream, digest)) {
            byte[] buffer = new byte[4096];
            while (dis.read(buffer) != -1) {
                // Read file data
            }
        }
        byte[] hashBytes = digest.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}
