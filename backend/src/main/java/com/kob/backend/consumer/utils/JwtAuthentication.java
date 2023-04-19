package com.kob.backend.consumer.utils;

import com.kob.backend.utils.JwtUtil;
import io.jsonwebtoken.Claims;

// 一个工具类 封装 自己封装好的一个工具类 用于解析token
public class JwtAuthentication {
//    static 因为在调用是 是直接用类名调用的
    public static Integer getUserId(String token){
//        -1 表示不存在用户
        int userId = -1;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = Integer.parseInt(claims.getSubject());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return userId;
    }
}
