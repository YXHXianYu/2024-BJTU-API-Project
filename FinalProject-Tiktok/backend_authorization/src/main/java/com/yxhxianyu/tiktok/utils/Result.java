package com.yxhxianyu.tiktok.utils;

/**
 * @author YXH_XianYu
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
