package com.axway.runners.strava;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;


@Configuration

public class StravaConfiguration {

   // private final RestTemplateBuilder restTemplateBuilder;

//    RestTemplate oAuthRestTemplate(ClientRegistrationRepository clientRegistrationRepository) {
//        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("strava");

    @Autowired
    private StravaOauthClientConfig stravaOauthClientConfig;

        @Bean
        public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder){

            return restTemplateBuilder
                    .additionalInterceptors(new OAuthClientCredentialsRestTemplateInterceptor(stravaOauthClientConfig))
                    .setReadTimeout(Duration.ofSeconds(5))
                    .setConnectTimeout(Duration.ofSeconds(1))
                    .build();
        }

}
