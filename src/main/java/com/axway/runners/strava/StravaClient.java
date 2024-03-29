package com.axway.runners.strava;

import com.axway.runners.APIClientExcepton;
import com.axway.runners.model.User;
import com.axway.runners.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StravaClient {

    @Autowired
    @Qualifier("stravaClient")
    RestTemplate restTemplate;

    @Autowired
    private StravaOauthClientConfig stravaOauthClientConfig;

    @Autowired
    private UserService userService;

    private static Logger logger = LoggerFactory.getLogger(StravaClient.class);
    private ObjectMapper objectMapper = new ObjectMapper();

    public String getAthlete(User user) {

        HttpHeaders headers = new HttpHeaders();
        refreshToken(user, headers);
        HttpEntity requestGet = new HttpEntity(headers);
        URI uri = UriComponentsBuilder.fromUriString("https://www.strava.com/api/v3/athlete").build().toUri();
        //refreshToken()
        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, requestGet, String.class);
        int statusCode = responseEntity.getStatusCodeValue();
        if (statusCode == 200) {
            DocumentContext documentContext = JsonPath.parse(responseEntity.getBody());
            String id = documentContext.read("$.id", String.class);
            return id;
        }
        return null;

    }


    public void deAuthorize(User user) {

        HttpHeaders headers = new HttpHeaders();
        refreshToken(user, headers);
        URI uri = UriComponentsBuilder.fromUriString("https://www.strava.com/oauth/deauthorize").queryParam("access_token", user.getOAuthToken().getAccess_token()).build().toUri();
        //refreshToken()
        HttpEntity<String> request =
            new HttpEntity<String>("");
        //HttpEntity requestGet = new HttpEntity();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(uri, request, String.class);
        if (responseEntity.getStatusCodeValue() == 200) {
            user.setVersion(System.currentTimeMillis());
            userService.save(user);
            user.setOAuthToken(null);
        }
        logger.info("DeAuthorize response : {}", responseEntity.getBody());
    }

    public void refreshToken(User user, HttpHeaders httpHeaders) {
        OAuthToken oAuthToken = user.getOAuthToken();

        long expiry_at = oAuthToken.getExpires_at();
        Instant instant = Instant.ofEpochSecond(expiry_at);
        if (System.currentTimeMillis() < instant.toEpochMilli()) {
            logger.info("Strava Token is not expired");
            httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + oAuthToken.getAccess_token());

        } else {
            logger.info("Strava Token Expired Refreshing the token ");


            OAuthToken newToken = refreshToken(oAuthToken);
            user.setOAuthToken(newToken);
            user.setVersion(System.currentTimeMillis());
            userService.save(user);

            httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + newToken.getAccess_token());

        }
    }

    private OAuthToken refreshToken(OAuthToken oldToken) {

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


    public Map<String, String> getActivities(User user, String id) {
        try {
            // HttpHeaders headers = setHeader(oAuthToken, email);
            HttpHeaders headers = new HttpHeaders();
            refreshToken(user, headers);
            HttpEntity requestGet = new HttpEntity(headers);
            URI uri = UriComponentsBuilder.fromUriString("https://www.strava.com/api/v3/activities/" + id).build().toUri();
            logger.info("Strava uri : {}", uri);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, requestGet, String.class);
            int statusCode = responseEntity.getStatusCodeValue();
            logger.info("Get Activities Response code : {}", statusCode);
            if (statusCode == 200) {
                Map<String, String> responseMap = new HashMap<>();
                DocumentContext documentContext = JsonPath.parse(responseEntity.getBody());
                String name = documentContext.read("name", String.class);
                String distance = documentContext.read("distance", String.class);
                String type = documentContext.read("type", String.class);
                String locationCountry = documentContext.read("location_country", String.class);
                String movingTime = documentContext.read("moving_time", String.class);
                responseMap.put("name", name);
                responseMap.put("distance", distance);
                responseMap.put("type", type);
                responseMap.put("location_country", locationCountry);
                responseMap.put("moving_time", movingTime);
                return responseMap;
            }
        } catch (APIClientExcepton | PathNotFoundException e) {
            logger.error("Error from strava : {}", e.getMessage());
            return null;
        }
        return null;

    }

    public List<StravaActivity> getActivitiesByDate(User user, long startDate, long endDate) {
        try {
            HttpHeaders headers = new HttpHeaders();
            refreshToken(user, headers);
            HttpEntity requestGet = new HttpEntity(headers);
            URI uri = UriComponentsBuilder.fromUriString("https://www.strava.com/api/v3/athlete/activities").
                queryParam("before", startDate).queryParam("after", endDate)
                .queryParam("per_page", 100).build().toUri();
            logger.info("Strava uri : {}", uri);

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, requestGet, String.class);
            int statusCode = responseEntity.getStatusCodeValue();
            logger.info("Get Activities  by date Response code : {}", statusCode);
            if (statusCode == 200) {
                return objectMapper.readValue(responseEntity.getBody(), new TypeReference<List<StravaActivity>>() {
                });

            }
        } catch (APIClientExcepton | PathNotFoundException | JsonProcessingException e) {
            logger.error("Error processing strava activities", e);
        }
        return null;
    }

    public boolean createSubscription(String uniqueKey) {
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

    public boolean getSubscription() {
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
        } catch (APIClientExcepton | PathNotFoundException e) {
            return false;
        }
        return false;
    }

    public ResponseEntity<OAuthToken> createToken(String code) {
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

//    private HttpHeaders setHeader(OAuthToken oAuthToken, String email) {
//        String accessToken = oAuthToken.getAccess_token();
//
//        HttpHeaders headers = new HttpHeaders();
//        //headers.add("Authorization", "Bearer " + accessToken);
//        headers.add("x-accessToken", accessToken);
//        headers.add("x_email", email);
//        headers.add("x-refreshToken", oAuthToken.getRefresh_token());
//        headers.add("x-exp", oAuthToken.getExpires_at() + "");
//        return headers;
//    }
}
