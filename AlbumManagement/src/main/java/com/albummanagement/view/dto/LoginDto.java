package com.albummanagement.view.dto;

import lombok.Data;

/**
 * 本类对应用户登录传递的参数
 * @Author:gj
 * @DateTime:2023/4/4 22:33
 **/
@Data
public class LoginDto {
    String username;
    String password;
}
