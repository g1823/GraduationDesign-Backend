package com.albummanagement.view.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 本类对应用户注册时的参数
 * @Author:gj
 * @DateTime:2023/4/4 22:26
 **/
@Data
public class RegisterDto {
    @NotNull(message = "用户名不能为空")
    String username;
    @NotNull(message = "密码不能为空")
    String password;
    @NotNull(message = "昵称不能为空")
    String nickname;
}
