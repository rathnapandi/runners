package com.axway.runners;

import com.axway.runners.model.Event;
import com.axway.runners.model.Participant;
import com.axway.runners.model.UnMatchedEventFeed;
import com.axway.runners.model.User;
import com.axway.runners.repo.UnMatchedEventFeedRepository;
import com.axway.runners.service.EventService;
import com.axway.runners.service.ParticipantService;
import com.axway.runners.service.UserService;
import com.axway.runners.strava.*;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@RestController
@Controller
public class HomeController {


    private Logger logger = LoggerFactory.getLogger(HomeController.class);


    @Autowired
    private String kibanaHost;

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


    @Autowired
    private UnMatchedEventFeedRepository unMatchedEventFeedRepository;

    //@Autowired
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/")
    public /*RedirectView*/ String home(OAuth2AuthenticationToken authToken, Model model) {
        User user = getUser(authToken);
        User existingUser = userService.getUser(user.getEmail());
        if (existingUser == null) {
            logger.info("Storing the user : {}", user.getEmail());
            existingUser = userService.save(user);

        } else {
            logger.info("User {} already exists", user.getEmail());
        }
        Iterable<Event> events = eventService.findAll();

        model.addAttribute("user", existingUser);
        model.addAttribute("events", events);
        model.addAttribute("navbar", "events");
        // return new RedirectView("/index.html");
        return "events";

    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/help")
    public String help(OAuth2AuthenticationToken authToken, Model model) {
        User user = getUser(authToken);
        model.addAttribute("user", user);
        model.addAttribute("navbar", "help");
        return "help";

    }

    public User getUser(OAuth2AuthenticationToken authToken) {
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
        User existingUser = userService.getUser(user.getEmail());
        if (existingUser == null) {

        }
        model.addAttribute("user", existingUser);
        String email = existingUser.getEmail();
        List<Participant> participants = participantService.findParticipantsByEventId(eventId);
        List<Participant> participantsByEmail = participantService.findParticipantsByEmailAAndEventId(email, eventId);
        Event event = eventService.findById(eventId);
        model.addAttribute("eventId", eventId);
        model.addAttribute("eventName", event.getName());
        model.addAttribute("participants", participants);
        model.addAttribute("participantsByEmail", participantsByEmail);
        model.addAttribute("navbar", "events");
        return "participant";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/events/{eventId}/strava")
    public String getStravaAuthzPage(OAuth2AuthenticationToken authToken, @PathVariable String eventId, Model model) {
        User user = getUser(authToken);
        model.addAttribute("user", user);
        Event event = eventService.findById(eventId);
        model.addAttribute("eventName", event.getName());
        model.addAttribute("eventId", eventId);
        // model.addAttribute("navbar", "participants");
        model.addAttribute("navbar", "events");
        return "stravaAuthz";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/events/{eventId}/add/participant/page")
    public String addParticipantPage(OAuth2AuthenticationToken authToken, @PathVariable String eventId, Participant participant, Model model) {
        User user = getUser(authToken);
        model.addAttribute("user", user);
        model.addAttribute("eventId", eventId);
        Event event = eventService.findById(eventId);
        model.addAttribute("event", event);
        model.addAttribute("navbar", "events");
        return "addParticipant";
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/events/{eventId}/update/participant/page/{participantId}")
    public String updateParticipantPage(OAuth2AuthenticationToken authToken, @PathVariable String eventId, @PathVariable String participantId, Participant participant, Model model) {
        User user = getUser(authToken);
        model.addAttribute("user", user);
        model.addAttribute("eventId", eventId);
        model.addAttribute("navbar", "events");
        participant = participantService.findById(participantId);
        Event event = eventService.findById(eventId);
        model.addAttribute("eventName", event.getName());
        model.addAttribute("participant", participant);
        return "updateParticipant";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/events/{eventId}/add/participant")
    public String addParticipant(OAuth2AuthenticationToken authToken, @PathVariable String eventId, @Valid Participant participant, BindingResult result, Model model) {

        User user = getUser(authToken);
        Event event = eventService.findById(eventId);
        if (result.hasErrors()) {
            model.addAttribute("eventId", eventId);
            model.addAttribute("navbar", "events");
            model.addAttribute("eventName", event.getName());
            model.addAttribute("user", user);
            return "addParticipant";
        }
        User existingUser = userService.getUser(user.getEmail());
        if (existingUser == null) {

        }
        participant.setEventId(eventId);
        participant.setEventName(event.getName());
        Participant savedParticipant = participantService.saveParticipant(participant);
        return "redirect:/events/" + eventId + "/participant";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/events/{eventId}/delete/participant/{id}")
    public String deleteParticipant(OAuth2AuthenticationToken authToken, @PathVariable String eventId, @PathVariable String id) {
        User user = getUser(authToken);
        User existingUser = userService.getUser(user.getEmail());
        if (existingUser == null) {

        }
        Participant existingParticipant = participantService.findById(id);
        if (existingParticipant == null) {
            //return new ResponseEntity<Event>(HttpStatus.EXPECTATION_FAILED);
        }

        participantService.deleteParticipant(existingParticipant);
        return "redirect:/events/" + eventId + "/participant";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/events/{eventId}/update/participant/{id}")
    public String updateParticipant(OAuth2AuthenticationToken authToken, @PathVariable String eventId, @PathVariable String id, Participant participant, Model model) {
        User user = getUser(authToken);
        User existingUser = userService.getUser(user.getEmail());
        if (existingUser == null) {

        }
        Participant existingParticipant = participantService.findById(id);
        if (existingParticipant == null) {
            // return new ResponseEntity<Event>(HttpStatus.EXPECTATION_FAILED);
        }
        Event event = eventService.findById(eventId);
        model.addAttribute("eventName", event.getName());
        existingParticipant.setEndTime(participant.getEndTime());
        existingParticipant.setStartTime(participant.getStartTime());
        existingParticipant.setVersion(System.currentTimeMillis());
        existingParticipant.setType(participant.getType());
        existingParticipant.setNote(participant.getNote());
        Participant updatedParticipant = participantService.saveParticipant(existingParticipant);
        // axwayClient.sendEmail(existingParticipant);
//        model.addAttribute("user", existingUser);
//        // model.addAttribute("navbar", "participants");
//        List<Participant> participants = participantService.findParticipantsByEventId(eventId);
//        // Event event = eventService.findById(eventId);
//        model.addAttribute("eventId", eventId);
//        model.addAttribute("participants", participants);
//        // model.addAttribute("navbar", "participants");
//        model.addAttribute("navbar", "events");
        return "redirect:/events/" + eventId + "/participant";
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/leaderboard")
    public String getleaderboard(OAuth2AuthenticationToken authToken, Model model) {
        User user = getUser(authToken);
        model.addAttribute("user", user);
        model.addAttribute("kibanaUrl", "https://fittogether.demo.axway.com:6080/app/dashboards#/view/69205bc0-3397-11ed-984e-19c6ca2fd017?embed=true&_g=(filters%3A!()%2CrefreshInterval%3A(pause%3A!t%2Cvalue%3A0)%2Ctime%3A(from%3A'2022-09-19T07%3A00%3A00.000Z'%2Cto%3Anow))");
        model.addAttribute("navbar", "leaderboard");
        return "leaderboard";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/activities")
    public String getActivities(OAuth2AuthenticationToken authToken, Model model) {
        User user = getUser(authToken);
        model.addAttribute("user", user);
        model.addAttribute("kibanaUrl", "https://fittogether.demo.axway.com:6080/app/dashboards#/view/f09fa6e0-3456-11ed-aaa9-61ce7bc08131?embed=true&_g=(filters%3A!()%2CrefreshInterval%3A(pause%3A!t%2Cvalue%3A0)%2Ctime%3A(from%3A'2022-09-19T07%3A00%3A00.000Z'%2Cto%3Anow))");
        model.addAttribute("navbar", "leaderboard");
        return "activities";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/dashboard")
    public String getDashboard(OAuth2AuthenticationToken authToken, Model model) {
        User user = getUser(authToken);
        model.addAttribute("user", user);
        model.addAttribute("kibanaUrl", "https://fittogether.demo.axway.com:6080/app/dashboards#/view/842b00f0-348c-11ed-aaa9-61ce7bc08131?embed=true&_g=(filters%3A!()%2CrefreshInterval%3A(pause%3A!t%2Cvalue%3A0)%2Ctime%3A(from%3A'2022-09-19T07%3A00%3A00.000Z'%2Cto%3Anow))");
        model.addAttribute("navbar", "leaderboard");
        return "dashboard";
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


    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/strava/authz")
    public String stravaAuthz(OAuth2AuthenticationToken authToken, @RequestParam(name = "eventId") String eventId) {

        Map<String, Object> attributes = authToken.getPrincipal().getAttributes();
        String email = (String) attributes.get("email");
        User user = userService.getUser(email);
        // OAuthToken oAuthToken = user.getOAuthToken();

        String uri = UriComponentsBuilder.fromHttpUrl(stravaOauthClientConfig.getAuthorization_uri()).queryParam("client_id", stravaOauthClientConfig.getClient_id()).queryParam("redirect_uri", stravaOauthClientConfig.getRedirect_uri())
            .queryParam("response_type", stravaOauthClientConfig.getGrant_type()).queryParam("approval_prompt", "auto").queryParam("scope", stravaOauthClientConfig.getScope()).queryParam("state", eventId).build().toUriString();
        return "redirect:" + uri;


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

        // model.addAttribute("navbar", "participants");
        model.addAttribute("navbar", "events");
        model.addAttribute("eventId", state);
        ResponseEntity<OAuthToken> token = stravaClient.createToken(code);
        if (token.getStatusCodeValue() != 200) {
            logger.error("Error in receiving oauth token");
            // return new RedirectView("/error?msg=Unable to read token");
        }
        OAuthToken oAuthToken = token.getBody();
        Map<String, Object> attributes = authToken.getPrincipal().getAttributes();
        String email = (String) attributes.get("email");
        User user = userService.getUser(email);
        model.addAttribute("user", user);
        user.setOAuthToken(oAuthToken);
        long time = System.currentTimeMillis();
        String athleteId = stravaClient.getAthlete(user);
        user.setAthleteId(athleteId);
        user.setVersion(time);
        boolean subscription = stravaClient.getSubscription();
        if (!subscription) {
            stravaClient.createSubscription(stravaKey);
        }
        List<Participant> participants = participantService.findParticipantsByEventId(state);
        model.addAttribute("participants", participants);
        userService.save(user);
        Event event = eventService.findById(state);
        model.addAttribute("eventName", event.getName());
        return "participant";

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/strava/sync/page")
    public String getSyncStrava(OAuth2AuthenticationToken authToken, @RequestParam String eventId, SyncActivity syncActivity, Model model) {

        model.addAttribute("navbar", "events");
        model.addAttribute("eventId", eventId);
        Map<String, Object> attributes = authToken.getPrincipal().getAttributes();
        String email = (String) attributes.get("email");
        User user = userService.getUser(email);
        model.addAttribute("user", user);
        Event event = eventService.findById(eventId);
        model.addAttribute("event", event);
        return "sync";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/strava/sync/{eventId}")
    public String syncStrava(OAuth2AuthenticationToken authToken, @PathVariable String eventId, @Valid SyncActivity syncActivity, Model model) {

        model.addAttribute("navbar", "events");
        model.addAttribute("eventId", eventId);
        Map<String, Object> attributes = authToken.getPrincipal().getAttributes();
        String email = (String) attributes.get("email");
        User user = userService.getUser(email);
        List<StravaActivity> activities = stravaClient.getActivitiesByDate(user, Instant.ofEpochMilli(syncActivity.getEndTime().getTime()).getEpochSecond(), Instant.ofEpochMilli(syncActivity.getStartTime().getTime()).getEpochSecond());
        model.addAttribute("activities", activities);
        model.addAttribute("user", user);
        Event event = eventService.findById(eventId);
        model.addAttribute("event", event);
        return "stravaSync";

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/strava/sync/{eventId}/submit")
    public String syncStravaSubmit(OAuth2AuthenticationToken authToken, @PathVariable String eventId, @RequestParam List<String> activities, Model model) {

        model.addAttribute("navbar", "events");
        model.addAttribute("eventId", eventId);
        Map<String, Object> attributes = authToken.getPrincipal().getAttributes();
        String email = (String) attributes.get("email");
        User user = userService.getUser(email);
        Map<String, String> status = new HashMap<>();
        for (String activityId : activities) {
            Map<String, String> activityDetail = stravaClient.getActivities(user, activityId);
            if (activityDetail == null) {
                status.put(activityId, "Unable to find activity id");
                continue;
            }
            UnMatchedEventFeed feed = unMatchedEventFeedRepository.findByActivityId(activityId);
            if(feed != null){
                feed.setVersion(System.currentTimeMillis());
            }else {
                feed = new UnMatchedEventFeed();
            }
            logger.info("Distance : {} , Moving Time {}", activityDetail.get("distance"), activityDetail.get("moving_time"));
            feed.setSenderName(user.getFirstName() + " " + user.getLastName());
            feed.setActivityId(activityId);
            feed.setDistance(null == activityDetail.get("distance") ? 0 : Float.parseFloat(activityDetail.get("distance")) / 1000);
            feed.setDuration(null == activityDetail.get("moving_time") ? 0 : Float.parseFloat(activityDetail.get("moving_time")) / 60);
            feed.setDescription(null == activityDetail.get("name") ? "Untitled" : activityDetail.get("name"));
            feed.setCountry(null == activityDetail.get("location_country") ? "US" : activityDetail.get("location_country"));
            feed.setType(null == activityDetail.get("type") ? "" : activityDetail.get("type"));
            Instant instant = Instant.now();
            feed.setEventTime(Date.from(instant));
            feed.setEventDateTime(Date.from(instant));
            unMatchedEventFeedRepository.save(feed);
            status.put(activityId, "Successfully synced");
        }
        Event event = eventService.findById(eventId);
        model.addAttribute("event", event);
        model.addAttribute("user", user);
        model.addAttribute("status", status);
        return "stravaSyncResponse";

    }

//    @PreAuthorize("hasRole('ROLE_USER')")
//    @GetMapping("/strava/deauthorize")
//    public ResponseEntity<?> stravaDauthorize(OAuth2AuthenticationToken authToken) {
//
//
//        Map<String, Object> attributes = authToken.getPrincipal().getAttributes();
//        String email = (String) attributes.get("email");
//        User user = userService.getUser(email);
//        stravaClient.deAuthorize(user);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//
//    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/strava/deauthorize")
    public String stravaDauthorize(OAuth2AuthenticationToken authToken, @RequestParam(name = "eventId") String eventId, Model model) {


        Map<String, Object> attributes = authToken.getPrincipal().getAttributes();
        String email = (String) attributes.get("email");

        User user = userService.getUser(email);
        user.setAthleteId(null);
        user.setVersion(System.currentTimeMillis());
        userService.save(user);
        stravaClient.deAuthorize(user);


        model.addAttribute("user", user);
        List<Participant> participants = participantService.findParticipantsByEventId(eventId);
        // Event event = eventService.findById(eventId);
        model.addAttribute("eventId", eventId);
        model.addAttribute("participants", participants);
        // model.addAttribute("navbar", "participants");
        model.addAttribute("navbar", "events");
        Event event = eventService.findById(eventId);
        model.addAttribute("eventName", event.getName());
        return "redirect:/events/" + eventId + "/participant";
        // return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    //https://www.strava.com/oauth/mobile/authorize?client_id=1234321&redirect_uri= YourApp%3A%2F%2Fwww.yourapp.com%2Fen-US&response_type=code&approval_prompt=auto&scope=activity%3Awrite%2Cread&state=test


}
