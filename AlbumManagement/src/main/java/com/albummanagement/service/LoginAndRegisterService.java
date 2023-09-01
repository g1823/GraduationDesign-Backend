package com.albummanagement.service;

import com.albummanagement.view.dto.LoginDto;
import com.albummanagement.view.dto.RegisterDto;
import com.albummanagement.view.vo.Result;

/**
 * 处理用户登录注册
 * @Author:gj
 * @DateTime:2023/4/4 22:40
 **/
public interface LoginAndRegisterService {

    /**
     * 处理用户注册
     * @param registerDto 这个类封装了用户名、密码、昵称
     * @return
     */
    Result register(RegisterDto registerDto);

    /**
     * 处理用户是否登录
     * @param token 用户token
     * @return
     */
    Result isLogin(String token);

    /**
     * 处理用户登录
     * @param loginDto
     * @return
     */
    Result login(LoginDto loginDto);
}
