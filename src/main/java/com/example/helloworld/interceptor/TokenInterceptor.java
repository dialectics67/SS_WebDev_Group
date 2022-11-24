package com.example.helloworld.interceptor;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.helloworld.constants.Consts;
import com.example.helloworld.constants.RedisConsts;
import com.example.helloworld.constants.TokenSettingConsts;
import com.example.helloworld.utils.JWTUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final StringRedisTemplate stringRedisTemplate;

    public TokenInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String, Object> res = new HashMap<>();
        res.put("code", 510004); // 设置状态 access_token 相关错误
        String access_token = request.getHeader("Authorization");
        if (!access_token.contains("Bearer ")) {
            res.put("code", 510001);
            res.put("message", "参数错误 Authorization中未查询到Bearer");
        } else {
            access_token = access_token.substring(access_token.indexOf("Bearer ") + "Bearer ".length());
            try {
                JWTUtils.verify(access_token, TokenSettingConsts.TYPE_ACCESS); // JWT验证令牌
                DecodedJWT decodedJWT = JWTUtils.getTokenPayload(access_token, TokenSettingConsts.TYPE_ACCESS);
                if (!Objects.equals(stringRedisTemplate.opsForValue().get(RedisConsts.uid_2_access_token_prefix + decodedJWT.getClaim(Consts.TOKEN_USERS_UID).asString()), access_token)) {
                    res.put("message", "token已被取代");
                } else {
                    // 将token中的payload加载进session
                    Integer uid = Integer.valueOf(decodedJWT.getClaim(Consts.TOKEN_USERS_UID).asString());
                    String username = decodedJWT.getClaim(Consts.TOKEN_USERS_NAME).asString();
                    request.getSession().setAttribute(Consts.SESSION_USERS_UID, uid);
                    request.getSession().setAttribute(Consts.SESSION_USERS_NAME, username);
                    return true;//放行请求
                }
            } catch (SignatureVerificationException e) {
                res.put("message", "无效签名");
            } catch (TokenExpiredException e) {
                res.put("message", "token过期");
            } catch (AlgorithmMismatchException e) {
                res.put("message", "token算法不一致");
            } catch (Exception e) {
                res.put("message", "token失效");
            }
        }
        //将map转化成json，response使用的是Jackson
        String json = new ObjectMapper().writeValueAsString(res);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().print(json);
        return false;
    }
}