package com.example.helloworld.utils;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Component
public class CookieUtil {

    public Optional<String> getCookieValue(HttpServletRequest request, String cookieName) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return Optional.ofNullable(cookie.getValue());
                }
            }
        }
        return Optional.empty();
    }

    public void setCookie(HttpServletResponse response, String cookieName, String value, int cookieMaxAge) {
        Cookie cookie = new Cookie(cookieName, value);
        cookie.setPath("/");
        cookie.setMaxAge(cookieMaxAge);
        response.addCookie(cookie);
    }

    public void deleteCookie(HttpServletResponse response, String cookieName) {
        setCookie(response, cookieName, null, 0);
    }
}
