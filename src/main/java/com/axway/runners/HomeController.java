package com.axway.runners;

import com.axway.runners.strava.OAuthToken;
import com.axway.runners.strava.StravaOauthClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
public class HomeController {



    private Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StravaOauthClientConfig stravaOauthClientConfig;

    //@Autowired
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/")
    public String helloWorld(OAuth2AuthenticationToken authToken) {
      //  ..logger.info(authToken.toString());
        logger.info(authToken.getAuthorities().toString());
        logger.info(authToken.getPrincipal().getAttributes().toString());
       // logger.info();
        return "Hello " + authToken.getPrincipal().getName() + "Your email id is : "+authToken.getPrincipal().getAttributes().get("unique_name");
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/strava")
    public String strava(OAuth2AuthenticationToken authToken) {
        //  ..logger.info(authToken.toString());
        logger.info(restTemplate.getClass().getCanonicalName());
        return "<a href=strava/login>Connect to strava</a>";

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/strava/login")
    public RedirectView stravaRedirect(OAuth2AuthenticationToken authToken) {
     String uri = UriComponentsBuilder.fromHttpUrl(stravaOauthClientConfig.getAuthorization_uri()).queryParam("client_id",stravaOauthClientConfig.getClient_id() ).queryParam("redirect_uri", stravaOauthClientConfig.getRedirect_uri())
                .queryParam("response_type", stravaOauthClientConfig.getGrant_type()).queryParam("approval_prompt", "auto").queryParam("scope", stravaOauthClientConfig.getScope()).build().toUriString();
    return new RedirectView(uri);


    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/strava/authorized")
    public String stravaAuthorize(@RequestParam String state, @RequestParam String code, @RequestParam String scope) {

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
       // logger.info("Create Backend API Response code: {}", strResponse.getStatusCodeValue());
        //return token.getBody();

        String accessToken = token.getBody().getAccess_token();

        headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+accessToken);
        HttpEntity  requestGet = new HttpEntity(headers);

        URI uri = UriComponentsBuilder.fromUriString("https://www.strava.com/api/v3/athlete/activities").build().toUri();
       // RequestEntity<?> requestEntity = new RequestEntity<>(HttpMethod.GET, uri);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, requestGet, String.class);

        return responseEntity.getBody();


    }


    ///strava/authorized

 //UriComponentsBuilder.fromHttpUrl(stravaOauthClientConfig.getAuthorization_uri()).queryParam("client_id",stravaOauthClientConfig.getClient_id(), )

    //https://www.strava.com/oauth/mobile/authorize?client_id=1234321&redirect_uri= YourApp%3A%2F%2Fwww.yourapp.com%2Fen-US&response_type=code&approval_prompt=auto&scope=activity%3Awrite%2Cread&state=test


}
