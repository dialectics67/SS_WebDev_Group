package com.example.helloworld.constants;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
/**
 * 用于保存与token相关的参数
 * 如果参数来自配置文件，需要有相应的getter setter, 切记这些方法不能为static
 */
public class TokenSettingConsts {

    public static String TYPE_ACCESS = "access";
    public static String TYPE_REFRESH = "refresh";
    public static String accessSignature;
    public static String refreshSignature;
    public static int accessTokenExpireTime;
    public static int refreshTokenExpireTime;


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

    public String getAccessSignature() {
        return accessSignature;
    }

    public void setAccessSignature(String accessSignature) {
        TokenSettingConsts.accessSignature = accessSignature;
    }

    public String getRefreshSignature() {
        return refreshSignature;
    }

    public void setRefreshSignature(String refreshSignature) {
        TokenSettingConsts.refreshSignature = refreshSignature;
    }

}
