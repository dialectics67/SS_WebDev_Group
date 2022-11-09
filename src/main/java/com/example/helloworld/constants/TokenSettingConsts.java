package com.example.helloworld.constants;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
public class TokenSettingConsts {
    public static String signature;
    public static int accessTokenExpireTime;
    public static int refreshTokenExpireTime;

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        TokenSettingConsts.signature = signature;
    }

    public int getAccessTokenExpireTime() {
        return accessTokenExpireTime;
    }

    public int getRefreshTokenExpireTime() {
        return refreshTokenExpireTime;
    }

    public void setAccessTokenExpireTime(int accessTokenExpireTime) {
        TokenSettingConsts.accessTokenExpireTime = accessTokenExpireTime;
    }

    public void setRefreshTokenExpireTime(int refreshTokenExpireTime) {
        TokenSettingConsts.refreshTokenExpireTime = refreshTokenExpireTime;
    }
}
