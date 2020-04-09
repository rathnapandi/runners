package com.axway.runners.strava;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

public class OAuthClientCredentialsRestTemplateInterceptor implements ClientHttpRequestInterceptor {
    private StravaOauthClientConfig stravaOauthClientConfig;


    public OAuthClientCredentialsRestTemplateInterceptor(StravaOauthClientConfig stravaOauthClientConfig){
        this.stravaOauthClientConfig = stravaOauthClientConfig;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {


        //request.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + "");
        return execution.execute(request, body);
    }



}