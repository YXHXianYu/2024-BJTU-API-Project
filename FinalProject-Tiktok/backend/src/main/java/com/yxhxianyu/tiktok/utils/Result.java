package com.yxhxianyu.tiktok.utils;

/**
 * @author YXH_XianYu
 * @date 2024/6/9 17:52
 * Struct is all you need
 **/
public class Result<T> {
    public String err;
    public T val;

    public Result(String err, T val) {
        this.err = err;
        this.val = val;
    }
}
