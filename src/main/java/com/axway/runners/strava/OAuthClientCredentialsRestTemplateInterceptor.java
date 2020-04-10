package com.axway.runners.strava;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Date;

public class OAuthClientCredentialsRestTemplateInterceptor implements ClientHttpRequestInterceptor {
    @Autowired
    private StravaOauthClientConfig stravaOauthClientConfig;

    @Autowired
    private RestTemplate restTemplate;


    public OAuthClientCredentialsRestTemplateInterceptor(StravaOauthClientConfig stravaOauthClientConfig){
        this.stravaOauthClientConfig = stravaOauthClientConfig;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {


        HttpHeaders httpHeaders = request.getHeaders();
        String accessToken = httpHeaders.getFirst("x-accessToken");
        if(accessToken != null) {
            long expiry_at = Long.parseLong( httpHeaders.getFirst("x-exp"));
            System.out.println(new Date(expiry_at));
            request.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        }
        ClientHttpResponse response = execution.execute(request, body);
        return response;
    }

    private OAuthToken refreshToken(OAuthToken oldToken){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> postParameters = new LinkedMultiValueMap<>();
        postParameters.add("client_id", stravaOauthClientConfig.getClient_id());
        postParameters.add("client_secret", stravaOauthClientConfig.getClient_secret());

        postParameters.add("refresh_token", oldToken.getRefresh_token());
        postParameters.add("grant_type", "refresh_token");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(
                postParameters, headers);
        ResponseEntity<OAuthToken> token = restTemplate.exchange(stravaOauthClientConfig.getToken_uri(), HttpMethod.POST, request, OAuthToken.class);

        return token.getBody();
    }



}