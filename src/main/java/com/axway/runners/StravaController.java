package com.axway.runners;

import com.axway.runners.model.User;
import com.axway.runners.service.UserService;
import com.axway.runners.strava.OAuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

// https://stackoverflow.com/questions/19238715/how-to-set-an-accept-header-on-spring-resttemplate-request

@RestController
@RequestMapping("/api")
public class StravaController {

    @Autowired
    private UserService userService;

    @Autowired
    @Qualifier("stravaClient")
    private RestTemplate restTemplate;


    @RequestMapping("/athlete/activities")
    public ResponseEntity<String> athleteActivities(OAuth2AuthenticationToken authToken) {
        Map<String, Object> attributes = authToken.getPrincipal().getAttributes();
        String email = (String) attributes.get("email");

        User user = userService.getUser(email);
        OAuthToken oAuthToken = user.getOAuthToken();
        String accessToken = oAuthToken.getAccess_token();

        HttpHeaders headers = new HttpHeaders();
        //headers.add("Authorization", "Bearer " + accessToken);
        headers.add("x-accessToken", accessToken);
        headers.add("x-refreshToken", oAuthToken.getRefresh_token());
        headers.add("x-exp", oAuthToken.getExpires_at()+"");
        HttpEntity requestGet = new HttpEntity(headers);

        URI uri = UriComponentsBuilder.fromUriString("https://www.strava.com/api/v3/athlete/activities").build().toUri();
        // RequestEntity<?> requestEntity = new RequestEntity<>(HttpMethod.GET, uri);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, requestGet, String.class);
        return  responseEntity;
    }



}

