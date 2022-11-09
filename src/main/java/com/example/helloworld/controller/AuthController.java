package com.example.helloworld.controller;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.helloworld.constants.Consts;
import com.example.helloworld.constants.RedisConsts;
import com.example.helloworld.constants.TokenSettingConsts;
import com.example.helloworld.entity.*;
import com.example.helloworld.service.*;
import com.example.helloworld.utils.CommonUtil;
import com.example.helloworld.utils.JWTUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 用户身份认证
 */

@Controller
@RequestMapping("/page")
public class AuthController {
    private final UserService userService;
    private final CommonUtil commonUtil;
    private final StringRedisTemplate stringRedisTemplate;

    public AuthController(UserService userService, CommonUtil commonUtil, StringRedisTemplate stringRedisTemplate) {
        this.userService = userService;
        this.commonUtil = commonUtil;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 登录
     */
    @GetMapping("/login")
    @ResponseBody
    public String login(String userId, String password, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        // 最终返回的对象
        JSONObject res = new JSONObject();
        // code 1001, 学号或者密码为空
        if (!StringUtils.hasLength(userId) || !StringUtils.hasLength(password)) {
            res.put("code", "1001");
            res.put("msg", "学号或者密码为空");
            return res.toString();
        }

        // code 1002, 学号错误
        Optional<UserEntity> userEntity = userService.findByUserId(Long.valueOf(userId));
        if (!userEntity.isPresent()) {
            res.put("code", "1002");
            res.put("msg", "学号密码错误");
            return res.toString();
        }
        // code 1002, 密码错误
        String md5Password = commonUtil.getMD5(password);
        if (!md5Password.equals(userEntity.get().getPassword())) {
            res.put("code", "1002");
            res.put("msg", "学号密码错误");
            return res.toString();
        }
        try {
            // 尝试签发token
            String userIdString = userEntity.get().getId().toString();
            String userName = userEntity.get().getUserName();
            List<String> tokens = get_tokens(userIdString, userName);
            String access_token = tokens.get(0);
            String refresh_token = tokens.get(1);
            //redis保存用户id->JWT的映射，防止同时存在多个有效的token
            stringRedisTemplate.opsForValue().set(RedisConsts.user_2_access_token_prefix + userIdString,
                    access_token, TokenSettingConsts.accessTokenExpireTime, TimeUnit.SECONDS);
            stringRedisTemplate.opsForValue().set(RedisConsts.user_2_refresh_token_prefix + userIdString,
                    refresh_token, TokenSettingConsts.refreshTokenExpireTime, TimeUnit.SECONDS);
            // 放置返回结果
            res.put("code", 0);
            res.put("msg", "认证成功");
            res.put("access_token", access_token);
            res.put("refresh_token", refresh_token);
        } catch (Exception e) {
            res.put("code", 1);
            res.put("msg", e.getMessage());
        }
        return res.toString();
    }

    private @NotNull List<String> get_tokens(String userId, String userName) {
        Map<String, String> payload = new HashMap<>();
        payload.put(Consts.TOKEN_USER_ID, userId);
        payload.put(Consts.TOKEN_USER_NAME, userName);
        //生成JWT令牌
        String access_token = JWTUtils.getToken(payload, TokenSettingConsts.accessTokenExpireTime);
        String refresh_token = JWTUtils.getToken(payload, TokenSettingConsts.refreshTokenExpireTime);
        List<String> res = new ArrayList<>();
        res.add(access_token);
        res.add(refresh_token);
        return res;
    }

    @GetMapping("/refresh_token")
    @ResponseBody
    public String refresh(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 1); //设置状态
        String old_refresh_token = request.getHeader("refresh_token");
        try {
            JWTUtils.verify(old_refresh_token); // JWT验证令牌
            DecodedJWT decodedJWT = JWTUtils.getToken(old_refresh_token);
            String userIdString = decodedJWT.getClaim(Consts.TOKEN_USER_ID).asString();
            String userName = decodedJWT.getClaim(Consts.TOKEN_USER_NAME).asString();
            if (!Objects.equals(stringRedisTemplate.opsForValue().get(RedisConsts.user_2_refresh_token_prefix + userIdString), old_refresh_token)) {
                map.put("msg", "refresh token已被取代");
            } else {// 验证通过
                // 重新生成token
                List<String> tokens = get_tokens(userIdString, userName);
                String new_access_token = tokens.get(0);
                String new_refresh_token = tokens.get(1);
                //redis保存用户id->JWT的映射，防止同时存在多个有效的token
                stringRedisTemplate.opsForValue().set(RedisConsts.user_2_access_token_prefix + userIdString,
                        new_access_token, TokenSettingConsts.accessTokenExpireTime, TimeUnit.SECONDS);
                stringRedisTemplate.opsForValue().set(RedisConsts.user_2_refresh_token_prefix + userIdString,
                        new_refresh_token, TokenSettingConsts.refreshTokenExpireTime, TimeUnit.SECONDS);
                // 防止返回结果
                map.put("code", 0);
                map.put("msg", "token刷新成功");
                map.put("access_token", new_access_token);
                map.put("refresh_token", new_refresh_token);
            }
        } catch (SignatureVerificationException e) {
            e.printStackTrace();
            map.put("msg", "无效签名");
        } catch (TokenExpiredException e) {
            e.printStackTrace();
            map.put("msg", "token过期");
        } catch (AlgorithmMismatchException e) {
            e.printStackTrace();
            map.put("msg", "token算法不一致");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "token失效");
        }
        //将map转化成json，response使用的是Jackson
        return new ObjectMapper().writeValueAsString(map);
    }

    @GetMapping("/please/login")
    @ResponseBody
    public String pleaseLogin(HttpServletResponse response) {
        JSONObject res = new JSONObject();
        res.put("code", "1001");
        res.put("msg", "请登录");
        return res.toString();
    }

    @GetMapping("/hello")
    @ResponseBody
    public String index(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        return "Hello " + session.getAttribute(Consts.SESSION_USER_NAME) + " 您已经登录";
    }
}
