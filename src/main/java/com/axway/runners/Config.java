package com.axway.runners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.time.Duration;
import java.util.Arrays;

@Configuration
public class Config {

    private static Logger log =  LoggerFactory.getLogger(   Config.class);

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.addAllowedHeader("*");
        configuration.setAllowedMethods(Arrays.asList("GET", "PUT", "POST", "DELETE"));
        source.registerCorsConfiguration("/**", configuration);
        log.debug("Registering CORS filter");
        source.registerCorsConfiguration("/api/**", configuration);
        source.registerCorsConfiguration("/management/**", configuration);
        return new CorsFilter(source);
    }

    @Bean
    @Qualifier("axwayClient")
    public RestTemplate restTemplateAxway(RestTemplateBuilder restTemplateBuilder){

        return restTemplateBuilder
                .setReadTimeout(Duration.ofSeconds(5))
                .setConnectTimeout(Duration.ofSeconds(1))
                .errorHandler(new APIClientErrorHandler())
                .build();
    }

}
