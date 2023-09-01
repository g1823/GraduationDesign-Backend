package com.albummanagement.view.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 本类为后端返回前端的统一返回类型
 * @Author:gj
 * @DateTime:2023/4/3 15:06
 **/
@Data
@AllArgsConstructor
public class Result {
    private int code;
    private String message;
    private Object data;

    //成功时的方法
    public static Result success(Object data) {
        return new Result( 200, "success", data);
    }

    //失败时的方法
    public static Result fail(int code, String msg){
        return new Result(code,msg,null);
    }
}
