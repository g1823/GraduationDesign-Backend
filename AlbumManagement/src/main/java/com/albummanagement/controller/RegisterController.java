package com.albummanagement.controller;

import com.albummanagement.service.LoginAndRegisterService;
import com.albummanagement.utils.MyLog;
import com.albummanagement.view.dto.RegisterDto;
import com.albummanagement.view.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 处理用户注册
 * @Author:gj
 * @DateTime:2023/4/4 22:44
 **/
@RestController
@RequestMapping("register")
@SuppressWarnings("all")
public class RegisterController {
    @Autowired
    LoginAndRegisterService loginAndRegisterService;

    @MyLog(moduleName = "注册模块",operationName = "注册")
    @PostMapping
    public Result register(@RequestBody RegisterDto registerDto){
        return loginAndRegisterService.register(registerDto);
    }
}
