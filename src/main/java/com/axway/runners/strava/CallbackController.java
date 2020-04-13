package com.axway.runners.strava;


import com.axway.runners.AxwayClient;
import com.axway.runners.Feed;
import com.axway.runners.User;
import com.axway.runners.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CallbackController {

    private static Logger logger = LoggerFactory.getLogger(CallbackController.class);

    @Autowired
    private UserService userService;

    @Value("${strava.client.key}")
    private String stravaKey;

    @Autowired
    private AxwayClient axwayClient;


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
        if (user != null) {
            logger.info("User : {}", user.getFirstName() + " " + user.getLastName() + " completed the run");
            Feed feed = new Feed();

            feed.setSenderName(user.getFirstName() + " " + user.getLastName());
            long eventTime = stravaAthlete.getEvent_time();
            long objectID = stravaAthlete.getObject_id();
            try {
                axwayClient.postMessageToTeams(user, "Run completed");
            }catch (Exception e){

            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
