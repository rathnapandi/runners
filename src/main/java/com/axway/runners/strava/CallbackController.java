package com.axway.runners.strava;


import com.axway.runners.*;
import com.axway.runners.service.EventService;
import com.axway.runners.service.ParticipantService;
import com.axway.runners.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
public class CallbackController {

    private static Logger logger = LoggerFactory.getLogger(CallbackController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private EventService eventService;

    @Value("${strava.client.key}")
    private String stravaKey;

    @Autowired
    private AxwayClient axwayClient;

    @Autowired
    private StravaClient stravaClient;


    @GetMapping(value = "/callback", produces = "application/json")
    public ResponseEntity<?> callback(@RequestParam(value = "hub.verify_token") String verify_token,
                                      @RequestParam(value = "hub.challenge") String hub_challenge, @RequestParam(value = "hub.mode") String hub_mode) {
        logger.info("Strava challenge code : {}", verify_token);

        if (!verify_token.equals(stravaKey)) {
            logger.info("Invalid token");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("{ \"hub.challenge\":\"" + hub_challenge + "\" }", HttpStatus.OK);
    }


    @PostMapping(value = "/callback")
    public ResponseEntity<?> receiveCallBack(@RequestBody StravaAthlete stravaAthlete) {
        logger.info("Event from strava : {}", stravaAthlete);
        String athleteId = stravaAthlete.getOwner_id() + "";
        User user = userService.findByAthleteId(athleteId);
        Participant participant = participantService.findByEmail(user.getEmail());
        Event event = eventService.findById(participant.getEventId());

        if (user != null) {
            logger.info("User : {}", user.getFirstName() + " " + user.getLastName() + " completed the run");
            Feed feed = new Feed();
            long eventTime = stravaAthlete.getEvent_time();
            String objType = stravaAthlete.getObject_type();
            long objectID = stravaAthlete.getObject_id();

            feed.setSenderName(user.getFirstName() + " " + user.getLastName());

            Instant instant = Instant.ofEpochSecond(eventTime);
            java.util.Date date = new java.util.Date();
            date.setTime(instant.toEpochMilli());

            feed.setMessage(feed.getSenderName() + " completed the activity at: " + date);
            feed.setTimeStamp(Long.toString(eventTime));
            event.setVersion(System.currentTimeMillis());
            event.addFeed(feed);
            eventService.saveEvent(event);

            try {
                Map<String, String> activityDetail = null;
                logger.info("Activity Type : {}", objType);
                if(objType.trim().equals("activity")) {
                    activityDetail = stravaClient.getActivities(user.getOAuthToken(), user.getEmail(), objectID);
                }
                axwayClient.postMessageToTeams(user, feed.getMessage(), stravaAthlete, date.toString(), activityDetail);
            }catch (Exception e){
                logger.error("Unhandled exception: " + e.getMessage());
            }
        } else {
            logger.error("Error parsing the User's activity. Activity is not Synchronized");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
