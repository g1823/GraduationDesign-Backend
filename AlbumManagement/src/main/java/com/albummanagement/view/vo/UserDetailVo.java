package com.albummanagement.view.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Author:gj
 * @DateTime:2023/4/17 15:34
 **/
@Data
public class UserDetailVo {
    /**
     * 用户账户，唯一表示
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 性别，0代表未知，1为男，2为女
     */
    private Integer sex;

    /**
     * 头像
     */
    private String avatar;


    /**
     * 个人简介
     */
    private String introduction;

    /**
     * 注册时间
     */
    private Date createTime;

    /**
     * 上次登录时间
     */
    private Date updateTime;
}
