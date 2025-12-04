package com.example.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Cross-domain configuration
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*"); // 1 Set the source IP address
        corsConfiguration.addAllowedHeader("*"); // 2 Set the source request header
        corsConfiguration.addAllowedMethod("*"); // 3 Set the access source request method
        source.registerCorsConfiguration("/**", corsConfiguration); // 4 Configure cross-domain settings for the interface
        return new CorsFilter(source);
    }
}