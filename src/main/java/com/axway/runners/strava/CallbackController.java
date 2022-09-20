package com.axway.runners.strava;


import com.axway.runners.AxwayClient;
import com.axway.runners.model.UnMatchedEventFeed;
import com.axway.runners.model.User;
import com.axway.runners.model.Participant;
import com.axway.runners.repo.UnMatchedEventFeedRepository;
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

@RestController
public class CallbackController {

    private static Logger logger = LoggerFactory.getLogger(CallbackController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private UnMatchedEventFeedRepository unMatchedEventFeedRepository;

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
        long eventTime = stravaAthlete.getEvent_time();
        String objType = stravaAthlete.getObject_type();
        long objectID = stravaAthlete.getObject_id();
        String aspectType = stravaAthlete.getAspect_type();
        String athleteId = stravaAthlete.getOwner_id() + "";
        logger.info("Activity Type : {} , Aspect type : {}", objType, aspectType);
        User user = userService.findByAthleteId(athleteId);
        if (user == null) {
            logger.info("Runner is not registered to the app");
            return new ResponseEntity<>(HttpStatus.OK);
        }
        List<Participant> participants = participantService.findByEmail(user.getEmail());
        if (objType.trim().equals("athlete") && aspectType.equals("create")) {
            axwayClient.postMessageToTeams(user, stravaAthlete);
            logger.info("Athlete Registration  Notification sent out to the Teams for the user : {} ", user.getFirstName() + " " + user.getLastName());
            return new ResponseEntity<>(HttpStatus.OK);
        }

        if (aspectType.equals("delete")) {
            logger.info("Ignoring the delete operation");
            return new ResponseEntity<>(HttpStatus.OK);
        }

        if (participants.isEmpty()) {
            logger.info("User is not registered to an event");
        } else {
            Participant participant = participants.get(0);
           // Event event = eventService.findById(participant.getEventId());
            logger.info("User's event found : {}", participant.getEventName());
            logger.info("User : {}", user.getFirstName() + " " + user.getLastName() + " completed the run");
        }
        try {
            UnMatchedEventFeed existingFeed = unMatchedEventFeedRepository.findByActivityId(objectID + "");
            if (existingFeed == null) {
                logger.info("Adding new feed");
                UnMatchedEventFeed feed = new UnMatchedEventFeed();
                feed.setSenderName(user.getFirstName() + " " + user.getLastName());
                Instant instant = Instant.ofEpochSecond(eventTime);
                Date date = new Date();
                date.setTime(instant.toEpochMilli());
                feed.setEventTime(Date.from(instant));
                feed.setMessage(feed.getSenderName() + " completed the activity at: " + date);
                feed.setTimeStamp(Long.toString(eventTime));
                feed.setEventDateTime(date);
                feed.setAthleteId(athleteId);
                Map<String, String> activityDetail = updateActivities(feed, user, objectID);
                unMatchedEventFeedRepository.save(feed);
                logger.info("feed successfully added");
                axwayClient.postMessageToTeams(user, feed.getMessage(), stravaAthlete, date.toString(), activityDetail);
            } else if(aspectType.equals("update")){
                logger.info("Updating Activity  id {}", objectID);
                unMatchedEventFeedRepository.delete(existingFeed);
                existingFeed.setId(null);
                existingFeed.setVersion(null);
                updateActivities(existingFeed, user, objectID);
                //existingFeed.setVersion(System.currentTimeMillis());
                unMatchedEventFeedRepository.save(existingFeed);
                logger.info("feed successfully updated");
            }
        } catch (Exception e) {
            logger.error("Error :", e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private  Map<String, String>  updateActivities(UnMatchedEventFeed feed, User user, long objectID){

        Map<String, String> activityDetail = stravaClient.getActivities(user, objectID);
        if( activityDetail == null){
            return null;
        }
        logger.info("Distance : {} , Moving Time {}", activityDetail.get("distance"), activityDetail.get("moving_time"));
        feed.setSenderName(user.getFirstName() + " " + user.getLastName());
        feed.setActivityId(Long.toString(objectID));
        feed.setDistance(null == activityDetail.get("distance") ? 0 : Float.parseFloat(activityDetail.get("distance")) / 1000);
        feed.setDuration(null == activityDetail.get("moving_time") ? 0 : Float.parseFloat(activityDetail.get("moving_time")) / 60);
        feed.setDescription(null == activityDetail.get("name") ? "Untitled" : activityDetail.get("name"));
        feed.setCountry(null == activityDetail.get("location_country") ? "US" : activityDetail.get("location_country"));
        feed.setType(null == activityDetail.get("type") ? "" : activityDetail.get("type"));
        return activityDetail;
    }


}
