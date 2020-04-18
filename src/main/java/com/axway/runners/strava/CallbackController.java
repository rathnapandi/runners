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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        List<Participant> participants = participantService.findByEmail(user.getEmail());
        long currentTime = System.currentTimeMillis();
        Optional<Participant> matchedParticipant = participants.parallelStream().filter(participant -> Long.parseLong(participant.getStartTime()) <= currentTime && Long.parseLong(participant.getStartTime()) >= currentTime).findFirst();
        if (matchedParticipant.isPresent()) {

            Participant participant = matchedParticipant.get();
            Event event = eventService.findById(participant.getEventId());
            logger.info("User's event found : {}", participant.getEventName());

            if (user != null) {
                logger.info("User : {}", user.getFirstName() + " " + user.getLastName() + " completed the run");
                Feed feed = new Feed();
                long eventTime = stravaAthlete.getEvent_time();
                String objType = stravaAthlete.getObject_type();
                long objectID = stravaAthlete.getObject_id();

                feed.setSenderName(user.getFirstName() + " " + user.getLastName());

                Instant instant = Instant.ofEpochSecond(eventTime);
                Date date = new Date();
                date.setTime(instant.toEpochMilli());

                try {
                    Map<String, String> activityDetail = null;
                    logger.info("Activity Type : {}", objType);
                    if (objType.trim().equals("activity")) {
                        activityDetail = stravaClient.getActivities(user, objectID);
                        feed.setActivityId(Long.toString(objectID));
                        feed.setDistance(null == activityDetail.get("distance") ? 0 : Float.parseFloat(activityDetail.get("distance")) / 1000);
                        feed.setDuration(null == activityDetail.get("moving_time") ? 0 : Float.parseFloat(activityDetail.get("moving_time")) / 60);
                        feed.setDescription(null == activityDetail.get("name") ? "Untitled" : activityDetail.get("name"));
                        feed.setCountry(null == activityDetail.get("location_country") ? "" : activityDetail.get("location_country"));
                        feed.setType(null == activityDetail.get("type") ? "" : activityDetail.get("type"));
                        feed.setEventTime(eventTime);
                        axwayClient.postMessageToTeams(user,
                                feed.getMessage(),
                                stravaAthlete,
                                date.toString(),
                                activityDetail);
                        logger.info("Activity Notification sent out to IB: " + feed.getMessage() + "Keys: " + activityDetail.keySet().toString() +
                                " Values: " + activityDetail.values().toString());
                    } else if (objType.trim().equals("athlete")) {
                        axwayClient.postMessageToTeams(user, stravaAthlete);
                        logger.info("Activity Notification sent out to IB: " + feed.getMessage());
                    }
                    feed.setMessage(feed.getSenderName() + " completed the activity at: " + date);
                    feed.setTimeStamp(Long.toString(eventTime));
                    feed.setEventDateTime(date);
                    feed.setAthleteId(athleteId);
                    event.setVersion(System.currentTimeMillis());
                    event.addFeed(feed);
                    eventService.saveEvent(event);
                } catch (NumberFormatException e) {
                    logger.error("NumberFormat Exception: " + e.getMessage());
                }
            } else {
                logger.error("Error parsing the User's activity. Activity is not Synchronized");
            }

        } else {
            logger.info("No matching participant ");
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
