package com.kl.wechat.utils;

public class Constants {

    // User States
    public static final int USER_STATE_DEFAULT = 0;
    public static final int USER_STATE_BANNED = 1;
    public static final int USER_STATE_ADMIN = 2;
    public static final int USER_STATE_SPIDER = 3;

    // Authority Checking
    public static final int AUTHORITY_CHECK_PASS = 0;
    public static final int AUTHORITY_CHECK_FAILED = 1;
    public static final int AUTHORITY_CHECK_USER_NOT_EXIST = 2;

    // Query
    public static final int RECORDS_PER_PAGE = 10;

    // Message
    public static final String MESSAGE_UNKNOWN_COMMAND = "该命令未被识别";
    public static final String MESSAGE_COMMIT_SUCCESS = "收到！感谢您的提交";
    public static final String MESSAGE_HELP = "Offer Show使用说明书\n\n" +
            "1. help: 帮助\n\n" +
            "2. commit: 提交一份Offer\n" +
            "  - 命令格式: commit company city position salary(int)\n\n" +
            "3. update: 更新一份Offer\n" +
            "  - 命令格式: update company city position salary(int)\n\n" +
            "4. delete: 删除一份Offer\n" +
            "  - 命令格式: delete company city position\n\n" +
            "5. query: 查询Offers\n" +
            "  - 命令格式: query [--company company] [--city city] [--position pos] [--salary-range min-max] [--page page(int)] [--sort-new] [--sort-salary]\n\n" +
            "6. group-commit: 提交多份Offer\n" +
            "  - 命令格式：group-commit co1 ci1 po1 sa1(int) [co2 ci2 po2 sa2(int) ...]\n\n" +
            "7. ban: 封禁用户\n" +
            "  - 命令格式: ban username\n\n" +
            "8. unban: 解禁用户\n" +
            "  - 命令格式: unban username\n\n" +
            "备注: 命令格式中的[]表示可选项,如果一个参数末尾带有(int),则表示它为一个整数类型。";
}