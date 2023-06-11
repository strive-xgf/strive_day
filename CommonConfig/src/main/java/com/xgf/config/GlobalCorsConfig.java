package com.xgf.config;

import com.xgf.common.LogUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;


/**
 * @author strive_day
 * @create 2023-06-11 22:16
 * @description 全局跨域问题配置
 */

@Configuration
public class GlobalCorsConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CorsFilter corsFilter() {

        LogUtil.info("===== GlobalCorsConfig corsFilter execute");

        CorsConfiguration config = new CorsConfiguration();

        // 配置允许跨域请求的来源模式
//        config.setAllowedOriginPatterns(Collections.singletonList("*"));
        config.setAllowedOriginPatterns(Arrays.asList(
                "http://localhost:8080",
                "http://localhost:8612"
        ));
        // 配置允许跨域请求的来源
//        config.addAllowedOrigin(CorsConfiguration.ALL);
        // 配置允许跨域请求的请求头
        config.addAllowedHeader(CorsConfiguration.ALL);
        // 配置允许跨域请求的HTTP方法
        config.addAllowedMethod(CorsConfiguration.ALL);
        // 配置允许服务端返回的响应头
        config.addExposedHeader(CorsConfiguration.ALL);
        // 配置请求保持时长，单位秒（响应缓存）
        config.setMaxAge(3600L);
        // 配置允许客户端发送包含凭证信息的跨域请求，如 Cookie 等认证信息
        config.setAllowCredentials(true);

        // 配置基于 url 配置的 cors 配置源
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        // 配置对所有的请求路径都启用CORS配置
        configSource.registerCorsConfiguration("/**", config);

        // CorsFilter
        return new CorsFilter(configSource);
    }




}

