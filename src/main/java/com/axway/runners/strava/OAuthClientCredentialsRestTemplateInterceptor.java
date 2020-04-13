package com.axway.runners.strava;

import com.axway.runners.User;
import com.axway.runners.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;

public class OAuthClientCredentialsRestTemplateInterceptor implements ClientHttpRequestInterceptor {
    @Autowired
    private StravaOauthClientConfig stravaOauthClientConfig;

    @Autowired
    private UserService userService;

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
            Instant instant = Instant.ofEpochSecond(expiry_at);
            if(System.currentTimeMillis() < instant.toEpochMilli()) {

                removeHttpHeaders(httpHeaders);
                httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
            }else {
                String email = httpHeaders.getFirst("x-accessToken");
                User user = userService.getUser(email);
                OAuthToken oAuthToken = user.getOAuthToken();

                OAuthToken newToken = refreshToken(oAuthToken);
                user.setOAuthToken(newToken);
                user.setVersion(System.currentTimeMillis());
                userService.save(user);
                removeHttpHeaders(httpHeaders);
                httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
            }
        }
        ClientHttpResponse response = execution.execute(request, body);
        return response;
    }

    private void removeHttpHeaders(HttpHeaders headers){
        headers.remove("x-accessToken");
        headers.remove("x_email");
        headers.remove("x-refreshToken");
        headers.remove("x-exp");
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