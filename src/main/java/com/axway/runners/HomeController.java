package com.axway.runners;

import com.axway.runners.model.Event;
import com.axway.runners.model.Participant;
import com.axway.runners.model.User;
import com.axway.runners.service.EventService;
import com.axway.runners.service.ParticipantService;
import com.axway.runners.service.UserService;
import com.axway.runners.strava.OAuthToken;
import com.axway.runners.strava.StravaClient;
import com.axway.runners.strava.StravaOauthClientConfig;
import com.azure.core.annotation.Delete;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

//@RestController
@Controller
public class HomeController {


    private Logger logger = LoggerFactory.getLogger(HomeController.class);


    @Autowired
    private UserService userService;

    @Autowired
    private StravaClient stravaClient;

    @Autowired
    private StravaOauthClientConfig stravaOauthClientConfig;

    @Value("${strava.client.key}")
    private String stravaKey;

    @Autowired
    private EventService eventService;

    @Autowired
    private ParticipantService participantService;

    //@Autowired
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/")
    public /*RedirectView*/ String home(OAuth2AuthenticationToken authToken, Model model) {
        User user = getUser(authToken);
        User existingUser = userService.getUser(user.getEmail());
        if (existingUser == null) {
            logger.info("Storing the user : {}", user.getEmail());
            userService.save(user);

        } else {
            logger.info("User {} already exists", user.getEmail());
        }
        Iterable<Event> events = eventService.findAll();

        model.addAttribute("user", user);
        model.addAttribute("events", events);
        model.addAttribute("navbar", "events");
       // return new RedirectView("/index.html");
        return "events";

    }

    public User getUser(OAuth2AuthenticationToken authToken){
        Map<String, Object> attributes = authToken.getPrincipal().getAttributes();
        logger.debug("Azure attributes : {}", attributes);
        String email = (String) attributes.get("email");
        String countryCode = (String) attributes.get("ctry");
        String firstName = (String) attributes.get("given_name");
        String lastName = (String) attributes.get("family_name");

        User user = new User();
        user.setCountryCode(countryCode);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return user;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/events/{eventId}/participant")
    public String getParticipantList(OAuth2AuthenticationToken authToken, @PathVariable String eventId, Model model) {
        User user = getUser(authToken);
        model.addAttribute("user", user);
        List<Participant> participants = participantService.findParticipantsByEventId(eventId);
       // Event event = eventService.findById(eventId);
        model.addAttribute("eventId", eventId);
        model.addAttribute("participants", participants);
       // model.addAttribute("navbar", "participants");
        model.addAttribute("navbar", "events");
        return "participant";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/events/{eventId}/add/participant/page")
    public String addParticipantPage(OAuth2AuthenticationToken authToken, @PathVariable String eventId, Participant participant, Model model) {
        User user = getUser(authToken);
        model.addAttribute("user", user);
        // model.addAttribute("navbar", "participants");
        model.addAttribute("navbar", "events");

        return "addParticipant";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/events/{eventId}/add/participant")
    public String addParticipant(OAuth2AuthenticationToken authToken, @PathVariable String eventId, Participant participant, Model model) {
        Participant savedParticipant = participantService.saveParticipant(participant);
//        axwayClient.sendEmail(participant);
//        return savedParticipant;
        User user = getUser(authToken);
        model.addAttribute("user", user);
        // model.addAttribute("navbar", "participants");
        model.addAttribute("navbar", "events");
        return "addParticipant";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/events/{eventId}/delete/participant/{id}")
    public String deleteParticipant(OAuth2AuthenticationToken authToken, @PathVariable String eventId,@PathVariable String id, Model model) {
        User user = getUser(authToken);
        Participant existingParticipant =  participantService.findById(id);
        if( existingParticipant == null){
            //return new ResponseEntity<Event>(HttpStatus.EXPECTATION_FAILED);
        }
        participantService.deleteParticipant(existingParticipant);
        model.addAttribute("user", user);
        // model.addAttribute("navbar", "participants");
        List<Participant> participants = participantService.findParticipantsByEventId(eventId);
        // Event event = eventService.findById(eventId);
        model.addAttribute("eventId", eventId);
        model.addAttribute("participants", participants);
        // model.addAttribute("navbar", "participants");
        model.addAttribute("navbar", "events");
        return "participant";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/events/{eventId}/update/participant/{id}")
    public String updateParticipant(OAuth2AuthenticationToken authToken, @PathVariable String eventId,@PathVariable String id,Participant participant,  Model model) {
        User user = getUser(authToken);
        Participant existingParticipant =  participantService.findById(id);
        if( existingParticipant == null){
           // return new ResponseEntity<Event>(HttpStatus.EXPECTATION_FAILED);
        }
        existingParticipant.setEndTime(participant.getEndTime());
        existingParticipant.setStartTime(participant.getStartTime());
        existingParticipant.setVersion(System.currentTimeMillis());
        Participant updatedParticipant = participantService.saveParticipant(existingParticipant);
       // axwayClient.sendEmail(existingParticipant);
        model.addAttribute("user", user);
        // model.addAttribute("navbar", "participants");
        List<Participant> participants = participantService.findParticipantsByEventId(eventId);
        // Event event = eventService.findById(eventId);
        model.addAttribute("eventId", eventId);
        model.addAttribute("participants", participants);
        // model.addAttribute("navbar", "participants");
        model.addAttribute("navbar", "events");
        return "participant";
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/leaderboard")
    public String getleaderboard(OAuth2AuthenticationToken authToken, Model model) {
        User user = getUser(authToken);
        model.addAttribute("user", user);
        model.addAttribute("navbar", "leaderboard");
        return "leaderboard";
    }



    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/strava")
    public String strava(OAuth2AuthenticationToken authToken) {
        //  ..logger.info(authToken.toString());
        // logger.info(restTemplate.getClass().getCanonicalName());
        return "<a href=strava/login>Connect to strava</a>";

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/strava/login")
    public RedirectView stravaRedirect(OAuth2AuthenticationToken authToken) {

        Map<String, Object> attributes = authToken.getPrincipal().getAttributes();
        String email = (String) attributes.get("email");
        User user = userService.getUser(email);
        // OAuthToken oAuthToken = user.getOAuthToken();

        String uri = UriComponentsBuilder.fromHttpUrl(stravaOauthClientConfig.getAuthorization_uri()).queryParam("client_id", stravaOauthClientConfig.getClient_id()).queryParam("redirect_uri", stravaOauthClientConfig.getRedirect_uri())
            .queryParam("response_type", stravaOauthClientConfig.getGrant_type()).queryParam("approval_prompt", "auto").queryParam("scope", stravaOauthClientConfig.getScope()).build().toUriString();
        return new RedirectView(uri);


    }

//    @PreAuthorize("hasRole('ROLE_USER')")
//    @RequestMapping("/strava/authorized")
//    public RedirectView stravaAuthorize(OAuth2AuthenticationToken authToken, @RequestParam String state, @RequestParam String code, @RequestParam String scope) {
//
//
//        ResponseEntity<OAuthToken> token = stravaClient.createToken(code);
//        if (token.getStatusCodeValue() != 200) {
//            logger.error("Error in receiving oauth token");
//            return new RedirectView("/error?msg=Unable to read token");
//        }
//        OAuthToken oAuthToken = token.getBody();
//        Map<String, Object> attributes = authToken.getPrincipal().getAttributes();
//        String email = (String) attributes.get("email");
//        User user = userService.getUser(email);
//
//        user.setOAuthToken(oAuthToken);
//        long time = System.currentTimeMillis();
//        String athleteId = stravaClient.getAthlete(user);
//        user.setAthleteId(athleteId);
//        user.setVersion(time);
//
//        boolean subscription = stravaClient.getSubscription();
//        if (!subscription) {
//            stravaClient.createSubscription(stravaKey);
//        }
//        userService.save(user);
//        return new RedirectView("/success.html");
//
//    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/strava/authorized")
    public String stravaAuthorize(OAuth2AuthenticationToken authToken, @RequestParam String state, @RequestParam String code, @RequestParam String scope, Model model) {


        ResponseEntity<OAuthToken> token = stravaClient.createToken(code);
        if (token.getStatusCodeValue() != 200) {
            logger.error("Error in receiving oauth token");
           // return new RedirectView("/error?msg=Unable to read token");
        }
        OAuthToken oAuthToken = token.getBody();
        Map<String, Object> attributes = authToken.getPrincipal().getAttributes();
        String email = (String) attributes.get("email");
        User user = userService.getUser(email);

        user.setOAuthToken(oAuthToken);
        long time = System.currentTimeMillis();
        String athleteId = stravaClient.getAthlete(user);
        user.setAthleteId(athleteId);
        user.setVersion(time);

        boolean subscription = stravaClient.getSubscription();
        if (!subscription) {
            stravaClient.createSubscription(stravaKey);
        }
        userService.save(user);
        return "participant";

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/strava/deauthorize")
    public ResponseEntity<?> stravaDauthorize(OAuth2AuthenticationToken authToken) {


        Map<String, Object> attributes = authToken.getPrincipal().getAttributes();
        String email = (String) attributes.get("email");
        User user = userService.getUser(email);
        stravaClient.deAuthorize(user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }


    ///strava/authorized

    //UriComponentsBuilder.fromHttpUrl(stravaOauthClientConfig.getAuthorization_uri()).queryParam("client_id",stravaOauthClientConfig.getClient_id(), )

    //https://www.strava.com/oauth/mobile/authorize?client_id=1234321&redirect_uri= YourApp%3A%2F%2Fwww.yourapp.com%2Fen-US&response_type=code&approval_prompt=auto&scope=activity%3Awrite%2Cread&state=test


}
