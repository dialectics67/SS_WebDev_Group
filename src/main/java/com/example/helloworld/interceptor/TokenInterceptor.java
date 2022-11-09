package com.example.helloworld.interceptor;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.helloworld.constants.Consts;
import com.example.helloworld.constants.RedisConsts;
import com.example.helloworld.service.UserService;
import com.example.helloworld.utils.CookieUtil;
import com.example.helloworld.utils.JWTUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Component
public class TokenInterceptor implements HandlerInterceptor {
    private final CookieUtil cookieUtil;
    private final UserService userService;
    private final StringRedisTemplate stringRedisTemplate;

    public TokenInterceptor(UserService userService, CookieUtil cookieUtil, StringRedisTemplate stringRedisTemplate) {
        this.userService = userService;
        this.cookieUtil = cookieUtil;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 1);//设置状态
        String access_token = request.getHeader("access_token");
        try {
            JWTUtils.verify(access_token); // JWT验证令牌
            DecodedJWT decodedJWT = JWTUtils.getToken(access_token);
            if (!Objects.equals(stringRedisTemplate.opsForValue().get(RedisConsts.user_2_access_token_prefix + decodedJWT.getClaim(Consts.TOKEN_USER_ID).asString()), access_token)) {
                map.put("msg", "token已被取代");
            } else {
                // 将token中的payload加载进session
                Long userId = Long.valueOf(decodedJWT.getClaim(Consts.TOKEN_USER_ID).asString());
                String userName = decodedJWT.getClaim(Consts.TOKEN_USER_NAME).asString();
                request.getSession().setAttribute(Consts.SESSION_USER_ID, userId);
                request.getSession().setAttribute(Consts.SESSION_USER_NAME, userName);
                return true;//放行请求
            }
        } catch (SignatureVerificationException e) {
//            e.printStackTrace();
            map.put("msg", "无效签名");
        } catch (TokenExpiredException e) {
//            e.printStackTrace();
            map.put("msg", "token过期");
        } catch (AlgorithmMismatchException e) {
//            e.printStackTrace();
            map.put("msg", "token算法不一致");
        } catch (Exception e) {
//            e.printStackTrace();
            map.put("msg", "token失效");
        }
        //将map转化成json，response使用的是Jackson
        String json = new ObjectMapper().writeValueAsString(map);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().print(json);
        return false;
    }
}