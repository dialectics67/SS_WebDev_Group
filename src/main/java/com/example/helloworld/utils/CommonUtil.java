package com.example.helloworld.utils;

import com.example.helloworld.entity.UserEntity;
import com.example.helloworld.webObject.TeamMember;
import com.example.helloworld.webObject.WebSubmission;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Component
public class CommonUtil {
    public String getMD5(String origin) {
        return DigestUtils.md5DigestAsHex(origin.getBytes());
    }

    /**
     * 获得用于前端显示的user的json，使用utf8编码，过滤掉一些字段
     *
     * @param userEntity
     * @return
     */
    public String getWebUser(UserEntity userEntity) throws UnsupportedEncodingException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", userEntity.getUserId());
        jsonObject.put("userName", userEntity.getUserName());
        jsonObject.put("userSex", userEntity.getUserSex());
        jsonObject.put("phone", userEntity.getPhone());
        jsonObject.put("birthday", userEntity.getBirthday());
        jsonObject.put("checkCode", userEntity.getCheckCode());
        return URLEncoder.encode(String.valueOf(jsonObject), "utf-8");
    }
}
