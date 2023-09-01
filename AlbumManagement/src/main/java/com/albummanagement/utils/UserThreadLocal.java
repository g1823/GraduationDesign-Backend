package com.albummanagement.utils;

import com.albummanagement.dao.entity.User;
import com.albummanagement.view.vo.UserVo;

/**
 * @Author:gj
 * @DateTime:2023/4/14 20:41
 **/
public class UserThreadLocal {
    private static ThreadLocal<UserVo> userThreadLocal = new ThreadLocal<>();

    public static void remove() {
        userThreadLocal.remove();
    }

    public static UserVo getUserVo() {
        return userThreadLocal.get();
    }

    public static void setUserVo(UserVo userVo) {
        userThreadLocal.set(userVo);
    }
}
