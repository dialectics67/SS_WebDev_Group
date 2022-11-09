package com.example.helloworld.config;

import com.example.helloworld.interceptor.TokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final TokenInterceptor tokenInterceptor;

    public WebMvcConfig(TokenInterceptor tokenInterceptor) {
        this.tokenInterceptor = tokenInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration sessionInterceptorRegistry = registry.addInterceptor(tokenInterceptor);
        // 排除不需要拦截的路径
//        sessionInterceptorRegistry.excludePathPatterns("/page/doLogin");
//        sessionInterceptorRegistry.excludePathPatterns("/error");

        // 需要拦截的路径
        sessionInterceptorRegistry.addPathPatterns("/**");
        sessionInterceptorRegistry.excludePathPatterns("/page/login");
        sessionInterceptorRegistry.excludePathPatterns("/page/refresh_token");
        sessionInterceptorRegistry.excludePathPatterns("/page/please/login");

    }
}