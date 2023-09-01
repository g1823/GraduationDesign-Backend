package com.albummanagement.utils;

/**
 * 自定义异常
 * @Author:gj
 * @DateTime:2023/4/13 21:20
 **/
public class MyException extends RuntimeException {
    public MyException(String message) {
        super(message);
    }
}