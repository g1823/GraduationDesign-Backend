package com.albummanagement.utils;

import com.albummanagement.view.vo.UserVo;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author:gj
 * @DateTime:2023/4/5 0:00
 **/
@Component
public class RedisUtils {

    private static RedisTemplate<String, String> redisTemplate;

    @Autowired
    public  void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        RedisUtils.redisTemplate = redisTemplate;
    }

    /**
     * 这个方法用来向redis中存储用户登录信息
     * @param token token令牌
     * @param userVo 用户信息，包括用户名，昵称，头像
     * @return
     */
    public static boolean setToken(String token, UserVo userVo){
        /*
        void set(K key, V value, long timeout, TimeUnit unit)
        方法含义：新增一个字符串类型的值，并且设置变量值的过期时间。
        参数说名：
            key是键，value是值，timeout 过期时间，unit 过期时间单位。
            unit 过期时间单位取值如下：
                DAYS：天时间单元代表24小时
                HOURS：小时时间单元代表60分钟
                MICROSECONDS：微秒时间单元代表千分之一毫秒
                MILLISECONDS：毫秒时间单元代表千分之一秒
                MINUTES：分钟时间单元代表60秒
                NANOSECONDS：纳秒时间单元代表千分之一微秒
                SECONDS：时间单元代表1秒
         */
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(userVo),
                12, TimeUnit.HOURS);
        return true;
    }

    /**
     * 这个方法用来根据token令牌返回用户数据
     * @param token token令牌
     * @return  返回值则为对应的用户信息，如果为空说明已经过期
     */
    public static UserVo getUserByToken(String token){
        String s = redisTemplate.opsForValue().get("TOKEN_" + token);
        /*
            StringUtils.isEmpty(null) = true
            StringUtils.isEmpty("") = true
            StringUtils.isEmpty(" ") = false //注意在 StringUtils 中空格作非空处理
            StringUtils.isEmpty("   ") = false
            StringUtils.isEmpty("bob") = false
            StringUtils.isEmpty(" bob ") = false
         */
        if(StringUtils.isNotEmpty(s)){
            return JSON.parseObject(s,UserVo.class);
        }else{
            return null;
        }
    }
}
