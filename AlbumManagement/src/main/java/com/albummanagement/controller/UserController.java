package com.albummanagement.controller;

import com.albummanagement.service.UserService;
import com.albummanagement.view.dto.FindUserDTO;
import com.albummanagement.view.dto.PasswordDto;
import com.albummanagement.view.dto.UserDetailDto;
import com.albummanagement.view.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 处理与用户信息相关的逻辑
 *
 * @Author:gj
 * @DateTime:2023/4/17 15:25
 **/
@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping
    public Result getUserDetail() {
        return userService.getUserDetail();
    }



    @PostMapping("/changeAvatar")
    public Result changeAvatar(@RequestParam("file") MultipartFile file) {
        return userService.changeAvatar(file);
    }

    @PostMapping("/changePassword")
    public Result changePassword(@RequestBody() PasswordDto PasswordDto) {
        return userService.changePassword(PasswordDto);
    }

    @PostMapping
    public Result changeUserMessage(@RequestBody() UserDetailDto userDetailDto) {
        return userService.changeUserMessage(userDetailDto);
    }

    @PostMapping("/list")
    public Result getUser(@RequestBody FindUserDTO findUserDTO){
        return userService.findUser(findUserDTO);
    }


}
