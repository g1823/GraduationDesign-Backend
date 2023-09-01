package com.albummanagement.utils;


import io.jsonwebtoken.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 * @Author:gj
 * @DateTime:2023/4/4 11:52
 **/
public class JWTUtils {
    //key:密钥
    private final static String key = "!@#$%hiuhb!@#$%+%$#@!fugfjv%$#@!+g2418gj#@!";
    //失效时间，默认为12小时
    private final static long expireTime = 24 * 60 * 60 * 60 * 1000;
    /*
    * 一个JWT由三部分组成：
    * Header（头部） —— base64编码的Json字符串
    * Payload（载荷） —— base64编码的Json字符串
    * Signature（签名）—— 使用指定算法，通过Header和Payload加盐计算的字符串
    */
    //生成token
    public static String createToken(String username){
        //将用户ID存到Map里，之后放入载荷中
        Map<String,Object> claims = new HashMap<>();
        claims.put("userName",username);

        long currentTimeMillis = System.currentTimeMillis();
        return Jwts.builder()
                .setIssuedAt(new Date(currentTimeMillis))    // 设置签发时间
                .setExpiration(new Date(currentTimeMillis + expireTime))   // 设置过期时间
                .setClaims(claims)// body数据，要唯一，自行设置
                .signWith(generateKey())
                .compact();
        /*
        * compact() 生成JWT：
        * 1.载荷校验，
        * 2.获取key。如果是keyBytes则通过keyBytes及算法名生成key对象。
        * 3.将所使用签名算法写入header。如果使用压缩，将压缩算法写入header。
        * 4.将Json形式的header转为bytes，再Base64编码
        * 5.将Json形式的claims转为bytes，如果需要压缩则压缩，再进行Base64编码
        * 6.拼接header和claims。如果签名key为空，则不进行签名(末尾补分隔符" . ")；
        *   如果签名key不为空，以拼接的字符串作为参数，按照指定签名算法进行签名计算签名部分 sign(String jwtWithoutSignature)，
        *   签名部分同样也会进行Base64编码。
        * 7.返回完整JWT*/
    }

    //生成密钥
    private static Key generateKey() {
        //SecretKeySpec:从给定的字节数组构造一个秘密密钥。
        return new SecretKeySpec(key.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }

    //验证token
    public static boolean verifyToken(String token) {
        try {
            Jwts.parser().setSigningKey(generateKey()).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //解析token
    public static Map<String, Object> parseToken(String token) {
        return Jwts.parser()  // 得到DefaultJwtParser
                .setSigningKey(generateKey()) // 设置签名密钥
                .parseClaimsJws(token)
                .getBody();
    }
}
