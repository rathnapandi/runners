package com.axway.runners.strava;

import com.axway.runners.APIClientExcepton;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class StravaClient {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private StravaOauthClientConfig stravaOauthClientConfig;

    private static Logger logger = LoggerFactory.getLogger(StravaClient.class);

    public String getAthlete(OAuthToken oAuthToken) {

        HttpHeaders headers = setHeader(oAuthToken);
        HttpEntity requestGet = new HttpEntity(headers);
        URI uri = UriComponentsBuilder.fromUriString("https://www.strava.com/api/v3/athlete").build().toUri();
         RequestEntity<?> requestEntity = new RequestEntity<>(HttpMethod.GET, uri);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, requestGet, String.class);
        int statusCode = responseEntity.getStatusCodeValue();
        if (statusCode == 200) {
            DocumentContext documentContext = JsonPath.parse(responseEntity.getBody());
            String id = documentContext.read("$.id", String.class);
            return id;
        }
        return null;

    }

    public boolean createSubscription(String uniqueKey){
        String url = "https://www.strava.com/api/v3/push_subscriptions";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        postParameters.add("client_id", stravaOauthClientConfig.getClient_id());
        postParameters.add("client_secret", stravaOauthClientConfig.getClient_secret());

        postParameters.add("callback_url", stravaOauthClientConfig.getCallback_url());
        postParameters.add("verify_token", uniqueKey);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(
                postParameters, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        int statusCode = response.getStatusCodeValue();
        if (statusCode == 200) {
            logger.info("Strava subscription is successfully registered");
            logger.info("Response from Strava : {}", response.getBody());
            return true;
        }
        return false;
    }

    public boolean getSubscription(String userId){
        String url = "https://www.strava.com/api/v3/push_subscriptions";
        URI uri = UriComponentsBuilder.fromUriString(url).queryParam("client_id", stravaOauthClientConfig.getClient_id())
                .queryParam("client_secret", stravaOauthClientConfig.getClient_secret()).build().toUri();
        try {

            ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
            int statusCode = response.getStatusCodeValue();
            if (statusCode == 200) {

                logger.info("Response from Strava : {}", response.getBody());
                String eventId = JsonPath.parse(response.getBody()).read("$.[0].id", String.class);
                if (eventId != null)
                    return true;
            }
        }catch (APIClientExcepton e){
            return false;
        }
        return false;
    }

    public ResponseEntity<OAuthToken> createToken(String code){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> postParameters = new LinkedMultiValueMap<>();
        postParameters.add("client_id", stravaOauthClientConfig.getClient_id());
        postParameters.add("client_secret", stravaOauthClientConfig.getClient_secret());

        postParameters.add("code", code);
        postParameters.add("grant_type", "authorization_code");


        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(
                postParameters, headers);
        ResponseEntity<OAuthToken> token = restTemplate.exchange(stravaOauthClientConfig.getToken_uri(), HttpMethod.POST, request, OAuthToken.class);
        return token;

    }

    private HttpHeaders setHeader(OAuthToken oAuthToken) {
        String accessToken = oAuthToken.getAccess_token();

        HttpHeaders headers = new HttpHeaders();
        //headers.add("Authorization", "Bearer " + accessToken);
        headers.add("x-accessToken", accessToken);
        headers.add("x-refreshToken", oAuthToken.getRefresh_token());
        headers.add("x-exp", oAuthToken.getExpires_at() + "");
        return headers;
    }
}
