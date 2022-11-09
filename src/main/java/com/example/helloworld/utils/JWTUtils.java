package com.example.helloworld.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.helloworld.constants.RedisConsts;
import com.example.helloworld.constants.TokenSettingConsts;

import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class JWTUtils {
    /**
     * 生成token
     *
     * @param map //传入payload
     * @return 返回token
     */
    public static String getToken(Map<String, String> map, int expires) {
        JWTCreator.Builder builder = JWT.create();
        map.forEach((k, v) -> {
            builder.withClaim(k, v);
        });
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND, expires);
        builder.withExpiresAt(instance.getTime());
        return builder.sign(Algorithm.HMAC256(TokenSettingConsts.signature));
    }

    /**
     * 验证token
     *
     * @param token
     */
    public static void verify(String token) {
        JWT.require(Algorithm.HMAC256(TokenSettingConsts.signature)).build().verify(token);
    }

    /**
     * 获取token中payload
     *
     * @param token
     * @return
     */
    public static DecodedJWT getToken(String token) {
        return JWT.require(Algorithm.HMAC256(TokenSettingConsts.signature)).build().verify(token);
    }

    public static void setRedisAccessToken(String userId, String token) {

    }

    public static void setRedisRefreshToken(String userId, String token) {

    }

    public static boolean checkRedisAccessToken(String userId, String token) {
        return false;
    }

    public static boolean checkRedisRefreshToken(String userId, String token) {
        return false;
    }
}
