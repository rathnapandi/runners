package com.axway.runners;

import com.axway.runners.strava.StravaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AxwayClient {

    private static Logger logger = LoggerFactory.getLogger(StravaClient.class);


    @Autowired
    @Qualifier("axwayClient")
    private RestTemplate restTemplateAxway;

    public void postMessageToTeams(User user, String msg){
        String teamsURL= "https://prod-e4ec6c3369cdafa50169ce18e33d00bb.apicentral.axwayamplify.com/Fitogether-Notify_sandbox_flow_434167-/executions";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> map = new HashMap<>();
        map.put("userName", user.getFirstName() + " " + user.getLastName());
        map.put("message", msg);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplateAxway.postForEntity(teamsURL, entity, String.class);
        int statusCode = response.getStatusCodeValue();
        if (statusCode == 200) {
            logger.info("Response from IB : {}", response.getBody());
        }

    }
}
