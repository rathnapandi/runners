package com.axway.runners.strava;

import com.axway.runners.User;
import com.axway.runners.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class CallbackController {

    private static Logger logger = LoggerFactory.getLogger(CallbackController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;


    @GetMapping(value = "/callback", produces = "application/json")
    public ResponseEntity<?> callback( @RequestParam(value = "hub.verify_token") String verify_token,
                                                  @RequestParam(value = "hub.challenge") String hub_challenge, @RequestParam(value = "hub.mode") String hub_mode) {
        logger.info("Strava challenge code : {}", verify_token);
        User user = userService.findById(verify_token);
        if (user == null) {
            logger.info("Invalid token");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("{ \"hub.challenge\":\"" + verify_token + "\" }", HttpStatus.OK);
    }


    @PostMapping(value = "/callback ", produces = "application/json")
    public ResponseEntity<?> callBack(@RequestBody StravaAthlete stravaAthlete) {
        logger.info("Event from strava : {}", stravaAthlete);
        String athleteId = stravaAthlete.getOwner_id() + "";
        User user = userService.findByAthleteId(athleteId);
        if (user != null) {
            logger.info("User : {}", user.getFirstName() + " " + user.getLastName() + " completed the run");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
