package com.example.helloworld.controller;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.helloworld.constants.Consts;
import com.example.helloworld.constants.RedisConsts;
import com.example.helloworld.constants.TokenSettingConsts;
import com.example.helloworld.entity.AuthEntity;
import com.example.helloworld.service.AuthService;
import com.example.helloworld.utils.CommonUtil;
import com.example.helloworld.utils.JWTUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final Consts consts;
    private final CommonUtil commonUtil;
    private final StringRedisTemplate stringRedisTemplate;

    public AuthController(CommonUtil commonUtil, Consts consts, AuthService authService, StringRedisTemplate stringRedisTemplate) {
        this.commonUtil = commonUtil;
        this.consts = consts;
        this.authService = authService;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 生成access_token, refresh_token;
     * 会在redis中存储uid->access_token, refresh_token的映射，以保证access_token, refresh_token相对用户是唯一的
     *
     * @param uid
     * @param username
     * @return
     */
    private List<String> grantTokens(String uid, String username) {
        Map<String, String> payload = new HashMap<>();
        payload.put(Consts.TOKEN_USERS_UID, uid);
        payload.put(Consts.TOKEN_USERS_NAME, username);
        //生成JWT令牌
        String access_token = JWTUtils.getTokenPayload(payload, TokenSettingConsts.accessTokenExpireTime, TokenSettingConsts.TYPE_ACCESS);
        String refresh_token = JWTUtils.getTokenPayload(payload, TokenSettingConsts.refreshTokenExpireTime, TokenSettingConsts.TYPE_REFRESH);
        List<String> res = new ArrayList<>();
        res.add(access_token);
        res.add(refresh_token);
        // redis保存用户id->JWT的映射，防止同时存在多个有效的token
        stringRedisTemplate.opsForValue().set(RedisConsts.uid_2_access_token_prefix + uid,
                access_token, TokenSettingConsts.accessTokenExpireTime, TimeUnit.SECONDS);
        stringRedisTemplate.opsForValue().set(RedisConsts.uid_2_refresh_token_prefix + uid,
                refresh_token, TokenSettingConsts.refreshTokenExpireTime, TimeUnit.SECONDS);
        return res;
    }


    @PostMapping("/login")
    @ResponseBody
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        // 最终返回的对象
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> resData = new HashMap<>();
        res.put("data", resData);
        // 逻辑处理
        try {
            // code 511001, 用户名，密码是否为空
            if (!StringUtils.hasLength(username) || !StringUtils.hasLength(password)) {
                res.put("code", 511001);
                res.put("message", "学号或者密码为空");
                return new ObjectMapper().writeValueAsString(res);
            }
            // code 511002, 用户名，密码长度是否超出限制
            if (username.length() > Consts.MAX_LEN_USERNAME || password.length() > Consts.MAX_LEN_PASSWORD) {
                res.put("code", 511002);
                res.put("message", "用户名或者密码长度超限");
                return new ObjectMapper().writeValueAsString(res);
            }
            // 根据username查找auth，type先固定为零
            Optional<AuthEntity> optionalAuthEntity = authService.findByUsernameAndType(username, 0);
            // code 511003, 用户名不存在
            if (!optionalAuthEntity.isPresent()) {
                res.put("code", "511003");
                res.put("message", "用户名不存在");
                return new ObjectMapper().writeValueAsString(res);
            }
            AuthEntity authEntity = optionalAuthEntity.get();
            // code 511004, 密码错误
            String md5Password = commonUtil.getMD5(password);
            if (!md5Password.equals(authEntity.getPassword())) {
                res.put("code", "511004");
                res.put("message", "密码错误");
                return new ObjectMapper().writeValueAsString(res);
            }
            // 尝试签发token
            String uidString = authEntity.getUid().toString();
            List<String> tokens = grantTokens(uidString, username);
            String access_token = tokens.get(0);
            String refresh_token = tokens.get(1);

            // 放置返回结果
            res.put("code", 200);
            res.put("message", "操作成功");
            resData.put("access_token", access_token);
            resData.put("token_type", "bearer");
            resData.put("expires_in", TokenSettingConsts.accessTokenExpireTime);
            resData.put("scope", "app");
            resData.put("refresh_token", refresh_token);
        } catch (Exception e) {
            res.put("code", 510000);
            res.put("message", e.getMessage());
        }
        return new ObjectMapper().writeValueAsString(res);
    }


    @PostMapping("/logout")
    @ResponseBody
    public String logout(HttpSession session) throws JsonProcessingException {
        Map<String, Object> res = new HashMap<>();
        res.put("code", 200);
        res.put("message", "操作成功");
        try {
            // 删除redis中对应的缓存
            Integer uid = (Integer) session.getAttribute(Consts.SESSION_USERS_UID);
            stringRedisTemplate.delete(RedisConsts.uid_2_access_token_prefix + uid);
            stringRedisTemplate.delete(RedisConsts.uid_2_refresh_token_prefix + uid);
        } catch (Exception e) {
            res.put("code", 510000);
            res.put("message", e.getMessage());
        }
        return new ObjectMapper().writeValueAsString(res);
    }

    @PostMapping("/refreshtoken")
    @ResponseBody
    public String refresh(@RequestBody Map<String, String> requestBody, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> resData = new HashMap<>();
        res.put("data", resData);
        String oldRefreshToken = requestBody.get("refresh_token");
        res.put("code", 510005); // refresh_token 认证失败
        try {
            JWTUtils.verify(oldRefreshToken, TokenSettingConsts.TYPE_REFRESH); // JWT验证令牌
            DecodedJWT decodedJWT = JWTUtils.getTokenPayload(oldRefreshToken, TokenSettingConsts.TYPE_REFRESH);
            String uidString = String.valueOf((Integer) session.getAttribute(Consts.SESSION_USERS_UID)); // 从access token获取uid
            String username = (String) session.getAttribute(Consts.SESSION_USERS_NAME); // 从access token获取username
            if (!Objects.equals(stringRedisTemplate.opsForValue().get(RedisConsts.uid_2_refresh_token_prefix + uidString), oldRefreshToken)) {
                res.put("msg", "refresh token已被取代");
            } else {// 验证通过
                // 重新生成token
                List<String> tokens = grantTokens(uidString, username);
                String new_access_token = tokens.get(0);
                String new_refresh_token = tokens.get(1);
                // 防止返回结果
                res.put("code", 200);
                res.put("message", "操作成功");
                resData.put("access_token", new_access_token);
                resData.put("token_type", "bearer");
                resData.put("expires_in", TokenSettingConsts.accessTokenExpireTime);
                resData.put("scope", "app");
                resData.put("refresh_token", new_refresh_token);
            }
        } catch (SignatureVerificationException e) {
            res.put("message", "refresh token 无效签名");
        } catch (TokenExpiredException e) {
            res.put("message", "refresh token");
        } catch (AlgorithmMismatchException e) {
            res.put("message", "refresh token算法不一致");
        } catch (Exception e) {
            res.put("message", "refresh token失效");
        }
        //将map转化成json，response使用的是Jackson
        return new ObjectMapper().writeValueAsString(res);
    }


    @GetMapping("/please")
    @ResponseBody
    public String pleaseLogin(HttpServletResponse response) {
        JSONObject res = new JSONObject();
        res.put("code", "0");
        res.put("message", "请登录");
        return res.toString();
    }

    @GetMapping("/hello")
    @ResponseBody
    public String index(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        return "Hello " + session.getAttribute(Consts.SESSION_USERS_NAME) + " 您已经登录";
    }
}
