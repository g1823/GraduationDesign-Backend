package com.albummanagement.controller;

import com.albummanagement.service.LoginAndRegisterService;
import com.albummanagement.view.dto.LoginDto;
import com.albummanagement.view.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 本类控制登录相关的模块
 * @Author:gj
 * @DateTime:2023/4/4 22:19
 **/
@RequestMapping("login")
@RestController
@SuppressWarnings("all")
public class LoginController {
    @Autowired
    LoginAndRegisterService loginAndRegisterService;

    //判断是否登录
    @GetMapping
    public Result isLogin(@RequestHeader(value = "token",defaultValue = "0") String token){
        return loginAndRegisterService.isLogin(token);
    }

    //用户登录
    @PostMapping
    public Result Login(@RequestBody LoginDto loginDto){
        return loginAndRegisterService.login(loginDto);
    }

}
