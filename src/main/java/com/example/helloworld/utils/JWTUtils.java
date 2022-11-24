package com.example.helloworld.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.helloworld.constants.TokenSettingConsts;

import java.util.Calendar;
import java.util.Map;
import java.util.Objects;

public class JWTUtils {
    /**
     * 生成token
     *
     * @param map //传入payload
     * @return 返回token
     */
    public static String getTokenPayload(Map<String, String> map, int expires, String type) {
        JWTCreator.Builder builder = JWT.create();
        map.forEach(builder::withClaim);
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND, expires);
        builder.withExpiresAt(instance.getTime());
        String signature = null;
        if (Objects.equals(type, TokenSettingConsts.TYPE_ACCESS)) {
            signature = TokenSettingConsts.accessSignature;
        } else {
            signature = TokenSettingConsts.refreshSignature;
        }
        return builder.sign(Algorithm.HMAC256(signature));
    }

    /**
     * 验证token
     *
     * @param token
     */
    public static void verify(String token, String type) {
        if (Objects.equals(type, TokenSettingConsts.TYPE_ACCESS)) {
            JWT.require(Algorithm.HMAC256(TokenSettingConsts.accessSignature)).build().verify(token);
        } else {
            JWT.require(Algorithm.HMAC256(TokenSettingConsts.refreshSignature)).build().verify(token);
        }
    }

    /**
     * 获取token中payload
     *
     * @param token
     * @return
     */
    public static DecodedJWT getTokenPayload(String token, String type) {
        if (Objects.equals(type, TokenSettingConsts.TYPE_ACCESS)) {
            return JWT.require(Algorithm.HMAC256(TokenSettingConsts.accessSignature)).build().verify(token);
        } else {
            return JWT.require(Algorithm.HMAC256(TokenSettingConsts.refreshSignature)).build().verify(token);
        }
    }

}
