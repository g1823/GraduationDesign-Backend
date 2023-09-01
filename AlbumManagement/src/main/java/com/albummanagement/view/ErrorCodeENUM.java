package com.albummanagement.view;

/**
 * 本类同意规定各种前后端的错误
 *
 * @Author:gj
 * @DateTime:2023/4/3 15:11
 **/
public enum ErrorCodeENUM {
    REPETITIVE_USERNAME(10001, "用户名已存在"),
    EMPTY_PARAMS(10002, "参数为空"),
    ERROR_PASSWORD(10003, "密码错误"),
    ERROR_USERNAME(10004, "用户名不存在"),
    LOGIN_EXPIRED(20001, "登录信息已经过期"),
    NOT_LOGIN(20002, "未登录"),
    SERVER_ERROR(50001, "服务器错误");

    private int code;
    private String msg;

    ErrorCodeENUM(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
