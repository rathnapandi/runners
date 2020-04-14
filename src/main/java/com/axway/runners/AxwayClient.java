package com.axway.runners;

import com.axway.runners.strava.StravaAthlete;
import com.axway.runners.strava.StravaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class AxwayClient {

    private static Logger logger = LoggerFactory.getLogger(StravaClient.class);


    @Autowired
    @Qualifier("axwayClient")
    private RestTemplate restTemplateAxway;

    @Async
    public CompletableFuture<Void> postMessageToTeams(User user, String msg, StravaAthlete stravaAthlete, String dateStr, Map<String, String> activityDetail) {
        String teamsURL = "https://prod-e4ec6c3369cdafa50169ce18e33d00bb.apicentral.axwayamplify.com/Fitogether-Notify_sandbox_flow_434167-/executions";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> map = new HashMap<>();
        map.put("userName", user.getFirstName() + " " + user.getLastName());
        map.put("object_type", stravaAthlete.getObject_type());
        map.put("event_time", dateStr);
        map.put("object_id", stravaAthlete.getObject_id());
        map.put("owner_id", stravaAthlete.getOwner_id());
        if( activityDetail != null) {
            map.put("activityDetail", activityDetail);
        }
        postMessage(teamsURL, map, headers);
        return CompletableFuture.completedFuture(null);

    }

    @Async
    public CompletableFuture<Void> sendEmail(Participant participant) {
        String url = "https://prod-e4ec6c3369cdafa50169ce18e33d00bb.apicentral.axwayamplify.com/Fitogether-Registration-Notify_sandbox_flow_434454-/executions";

        Calendar calendar = Calendar.getInstance();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> map = new HashMap<>();
        map.put("userName", participant.getFirstName() + " " + participant.getLastName());
        map.put("email", participant.getEmail());
        map.put("eventName", participant.getEventName());
        calendar.setTimeInMillis(participant.getStartTime());
        map.put("startTime", calendar.getTime());
        calendar.setTimeInMillis(participant.getEndTime());
        map.put("endTime", calendar.getTime());
        map.put("countryCode", participant.getCountryCode());
        postMessage(url, map, headers);
        return CompletableFuture.completedFuture(null);

    }

    private void postMessage(String url, Map<String, Object> map ,  HttpHeaders headers ){
        try {
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);
            ResponseEntity<String> response = restTemplateAxway.postForEntity(url, entity, String.class);
            int statusCode = response.getStatusCodeValue();
            logger.info("Status code from IB : ", statusCode);
            if (statusCode == 200 || statusCode == 201) {
                logger.info("Response from IB : {}", response.getBody());
            }
        }catch(APIClientExcepton e) {
            logger.error("Error from IB : {}", e.getMessage());
        }
    }
}
