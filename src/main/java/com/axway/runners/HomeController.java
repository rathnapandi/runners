package com.axway.runners;

import com.axway.runners.service.UserService;
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

import java.util.Map;

@RestController
public class HomeController {


    private Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private StravaOauthClientConfig stravaOauthClientConfig;

    //@Autowired
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/")
    public RedirectView home(OAuth2AuthenticationToken authToken) {
        Map<String, Object> attributes = authToken.getPrincipal().getAttributes();
        String email = (String) attributes.get("unique_name");
        String countryCode = (String) attributes.get("ctry");
        String firstName = (String) attributes.get("given_name");
        String lastName = (String) attributes.get("family_name");

        User user = new User();
        user.setCountryCode(countryCode);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        User existingUser = userService.getUser(email);
        if (existingUser == null) {
            logger.info("Storing the user : {}", email);
            userService.save(user);

        } else {
            logger.info("User {} already exists", email);
        }


        return new RedirectView("/app/public/index.html");

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
        String uri = UriComponentsBuilder.fromHttpUrl(stravaOauthClientConfig.getAuthorization_uri()).queryParam("client_id", stravaOauthClientConfig.getClient_id()).queryParam("redirect_uri", stravaOauthClientConfig.getRedirect_uri())
                .queryParam("response_type", stravaOauthClientConfig.getGrant_type()).queryParam("approval_prompt", "auto").queryParam("scope", stravaOauthClientConfig.getScope()).build().toUriString();
        return new RedirectView(uri);


    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/strava/authorized")
    public RedirectView stravaAuthorize(OAuth2AuthenticationToken authToken, @RequestParam String state, @RequestParam String code, @RequestParam String scope) {

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
        if (token.getStatusCodeValue() != 200) {
            logger.error("Error in receiving oauth token");
            return new RedirectView("/error?msg=Unable to read token");
        }
        // logger.info("Create Backend API Response code: {}", strResponse.getStatusCodeValue());
        //return token.getBody();
        OAuthToken oAuthToken = token.getBody();
        Map<String, Object> attributes = authToken.getPrincipal().getAttributes();
        String email = (String) attributes.get("unique_name");
        User user = userService.getUser(email);

        user.setOAuthToken(oAuthToken);
        long time = System.currentTimeMillis();
        user.setVersion(time);


        userService.save(user);
//        System.out.println(updatedUser.getOAuthToken());
//        logger.info(updatedUser.getVersion() + "");

        return new RedirectView("/app/public/index.html");


    }


    ///strava/authorized

    //UriComponentsBuilder.fromHttpUrl(stravaOauthClientConfig.getAuthorization_uri()).queryParam("client_id",stravaOauthClientConfig.getClient_id(), )

    //https://www.strava.com/oauth/mobile/authorize?client_id=1234321&redirect_uri= YourApp%3A%2F%2Fwww.yourapp.com%2Fen-US&response_type=code&approval_prompt=auto&scope=activity%3Awrite%2Cread&state=test


}
