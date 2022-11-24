package com.example.helloworld.config;

import com.example.helloworld.interceptor.TokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private final TokenInterceptor tokenInterceptor;

    public InterceptorConfig(TokenInterceptor tokenInterceptor) {
        this.tokenInterceptor = tokenInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration sessionInterceptorRegistry = registry.addInterceptor(tokenInterceptor);


        // 需要拦截的路径
        sessionInterceptorRegistry.addPathPatterns("/**");
        // 排除不需要拦截的路径
        sessionInterceptorRegistry.excludePathPatterns("/auth/login");
        sessionInterceptorRegistry.excludePathPatterns("/auth/please");

    }
}