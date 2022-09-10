package com.axway.runners.strava;

import com.axway.runners.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public class OAuthClientCredentialsRestTemplateInterceptor implements ClientHttpRequestInterceptor {
    @Autowired
    private StravaOauthClientConfig stravaOauthClientConfig;

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    private static Logger logger = LoggerFactory.getLogger(OAuthClientCredentialsRestTemplateInterceptor.class);


    public OAuthClientCredentialsRestTemplateInterceptor(StravaOauthClientConfig stravaOauthClientConfig) {
        this.stravaOauthClientConfig = stravaOauthClientConfig;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        logger.info("Request URI : {}", request.getURI());
        logger.info("Request Headers     : {}", request.getHeaders());
        ClientHttpResponse response = execution.execute(request, body);
        logger.info("Response status code : {} and Text : {}", response.getRawStatusCode(), response.getStatusText());
        logger.info("Response Headers     : {}", response.getHeaders());
        return response;

    }





}
