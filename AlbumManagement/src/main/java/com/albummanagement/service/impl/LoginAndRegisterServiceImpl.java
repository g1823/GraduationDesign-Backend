package com.albummanagement.service.impl;


import com.albummanagement.dao.entity.User;
import com.albummanagement.service.LoginAndRegisterService;
import com.albummanagement.service.UserService;
import com.albummanagement.utils.HttpContextUtils;
import com.albummanagement.utils.JWTUtils;
import com.albummanagement.utils.RedisUtils;
import com.albummanagement.view.dto.LoginDto;
import com.albummanagement.view.dto.RegisterDto;
import com.albummanagement.view.ErrorCodeENUM;
import com.albummanagement.view.vo.Result;
import com.albummanagement.view.vo.UserVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author:gj
 * @DateTime:2023/4/4 22:40
 **/
@Service
@SuppressWarnings("all")
public class LoginAndRegisterServiceImpl implements LoginAndRegisterService {

    @Autowired
    UserService userService;

    private String slot = "zxcv~!@#";

    /*
        流程：
            1.判断参数是否合法，不合法返回参数错误
            2.判断用户名是否存在，存在返回用户名错误
            3.创建新的用户，将信息填入数据库，对密码进行加密
            4.以用户名为基础，生成token
            5.将token以及用户信息存入redis数据库
            6.将token加入响应头并返回
     */
    @Override
    public Result register(RegisterDto registerDto) {
        String username = registerDto.getUsername();
        String password = registerDto.getPassword();
        String nickname = registerDto.getNickname();
        //用户名密码不能为空，不能为制表符和空格；昵称不能为空，但是可以为空格
        if (StringUtils.isBlank(username) && StringUtils.isBlank(password) && StringUtils.isEmpty(nickname)) {
            return Result.fail(ErrorCodeENUM.EMPTY_PARAMS.getCode(), ErrorCodeENUM.EMPTY_PARAMS.getMsg());
        }
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername, username);
        if (userService.getOne(lambdaQueryWrapper) == null ? false : true) {
            return Result.fail(ErrorCodeENUM.REPETITIVE_USERNAME.getCode(), ErrorCodeENUM.REPETITIVE_USERNAME.getMsg());
        }
        //对密码进行加密
        password = DigestUtils.md5Hex(password);
        Date date = new Date();
        User user = new User();
        user.setCreateTime(date);
        user.setUpdateTime(date);
        user.setUsername(username);
        user.setPassword(password);
        user.setNickname(nickname);
        if (userService.save(user)) {
            //此时表示注册成功，生成token，并将用户信息放入redis数据库
            String token = JWTUtils.createToken(username);
            UserVo userVo = new UserVo(user.getId(), username, nickname, date, user.getAvatar(), token);
            RedisUtils.setToken(token, userVo);//存入redis
            return Result.success(userVo);
        } else {
            return Result.fail(ErrorCodeENUM.SERVER_ERROR.getCode(), ErrorCodeENUM.SERVER_ERROR.getMsg());
        }
    }

    @Override
    public Result isLogin(String token) {
        UserVo userByToken = RedisUtils.getUserByToken(token);
        if (userByToken == null) {
            return Result.fail(ErrorCodeENUM.LOGIN_EXPIRED.getCode(), ErrorCodeENUM.LOGIN_EXPIRED.getMsg());
        } else {
            return Result.success(userByToken);
        }
    }

    @Override
    public Result login(LoginDto loginDto) {
        String username = loginDto.getUsername();
        String password = loginDto.getPassword();
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return Result.fail(ErrorCodeENUM.EMPTY_PARAMS.getCode(), ErrorCodeENUM.EMPTY_PARAMS.getMsg());
        }
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername, username);
        User user = userService.getOne(lambdaQueryWrapper);
        if (user == null) {
            return Result.fail(ErrorCodeENUM.ERROR_USERNAME.getCode(), ErrorCodeENUM.ERROR_USERNAME.getMsg());
        }
        password = DigestUtils.md5Hex(password);
        if (password.equals(user.getPassword())) {
            LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(User::getUsername, username);
            Date date = new Date();
            userService.update(lambdaUpdateWrapper.set(User::getUpdateTime, date));
            String token = JWTUtils.createToken(username);
            UserVo userVo = new UserVo(user.getId(), username, user.getNickname(), date, user.getAvatar(), token);
            RedisUtils.setToken(token, userVo);//存入redis
            return Result.success(userVo);
        }
        return Result.fail(ErrorCodeENUM.ERROR_PASSWORD.getCode(), ErrorCodeENUM.ERROR_PASSWORD.getMsg());
    }


}
