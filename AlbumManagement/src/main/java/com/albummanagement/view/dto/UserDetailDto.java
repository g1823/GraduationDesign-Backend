package com.albummanagement.view.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Author:gj
 * @DateTime:2023/4/30 10:55
 **/
@Data
public class UserDetailDto {
    /**
     * 用户账户，唯一表示
     */
    private String username;


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
     * 个人简介
     */
    private String introduction;

}
