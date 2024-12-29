package com.example.developerweb.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * packageName      : com.example.developerweb.common.config
 * fileName         : WebConfig
 * author           : YUN
 * date             : 2024. 9. 25.
 * description      : 정적리소스 매핑 제외 - html 에서 Controller 로 요청을 보낼때 정적 리소스 매핑 제외
 * ========================================================
 * DATE             AUTHOR              MEMO
 * ---------------------------------------------------------
 * 2024. 9. 25.     YUN                 @Configuration 을 이용한 정적 리소스 매핑 제외
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**", "/js/**", "/img/**")
                .addResourceLocations("classpath:/static/css/", "classpath:/static/js/", "classpath:/static/img/");
    }
}
