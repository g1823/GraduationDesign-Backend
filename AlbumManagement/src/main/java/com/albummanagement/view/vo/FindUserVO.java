package com.albummanagement.view.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

/**
 * @Author:gj
 * @DateTime:2023/5/14 20:40
 **/
@Data
public class FindUserVO {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

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
    private String sex;

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


}
