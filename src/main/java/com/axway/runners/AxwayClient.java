package com.axway.runners;

import com.axway.runners.strava.StravaAthlete;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class AxwayClient {

    private static Logger logger = LoggerFactory.getLogger(AxwayClient.class);


    @Autowired
    @Qualifier("axwayClient")
    private RestTemplate restTemplateAxway;

    @Value("${ib.notify.activities.url}")
    private String notifyActivitiesURL;

    @Value("${ib.notify.prerace.url}")
    private String notifyPreRaceURL;

    @Value("${ib.notify.email.url}")
    private String emailURL;

    private  DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");

    @Async
    public CompletableFuture<Void> postMessageToTeams(User user, String msg, StravaAthlete stravaAthlete, String dateStr, Map<String, String> activityDetail) {
       // String teamsURL = "https://prod-e4ec6c3369cdafa50169ce18e33d00bb.apicentral.axwayamplify.com/Fitogether-Notify-04132020_sandbox_flow_434712-/executions";
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
        postMessage(notifyActivitiesURL, map, headers);
        return CompletableFuture.completedFuture(null);

    }

    @Async
    public CompletableFuture<Void> postMessageToTeams(User user,  StravaAthlete stravaAthlete) {
       // String teamsURL = "https://prod-e4ec6c3369cdafa50169ce18e33d00bb.apicentral.axwayamplify.com/Fitogether-PreRace-Party_sandbox_flow_434470-/executions";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> map = new HashMap<>();
        map.put("userName", user.getFirstName() + " " + user.getLastName());
        map.put("owner_id", stravaAthlete.getOwner_id());
        postMessage(notifyPreRaceURL, map, headers);
        return CompletableFuture.completedFuture(null);

    }

   @Async
    public CompletableFuture<Void> sendEmail(Participant participant) {
       // String url = "https://prod-e4ec6c3369cdafa50169ce18e33d00bb.apicentral.axwayamplify.com/Fitogether-Registration-Notify_sandbox_flow_434454-/executions"
        logger.info("Sending email to : {}", participant.getEmail());
        logger.info(emailURL);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(participant.getStartTime()));
        OffsetDateTime utcStartTime  = calendar.toInstant().atOffset(ZoneOffset.UTC);
        calendar.setTimeInMillis(Long.parseLong(participant.getEndTime()));
        OffsetDateTime utcEndTime  = calendar.toInstant().atOffset(ZoneOffset.UTC);
        String msg = "Thank you " + participant.getFirstName() + " " + participant.getLastName() +
                " for registering with FiTogether \\n\\nWe are very excited you signed up with us. We are looking forward to your participation with this challenge\\n\\n " +
                "Important details to keep in mind for the upcoming challenge event on "
                + utcStartTime.getDayOfMonth() + "th "+ utcStartTime.getMonth() + "\\n\\nStart Time: " + utcStartTime.getHour()+ " " +utcStartTime.getMinute()
                +"\\n\\nEndTime: " + utcEndTime.getHour() + " " + utcEndTime.getMinute()
                + "\\n\\nEvent Name: " + participant.getEventName() + "\\n\\nCountry: "
                + participant.getCountryCode() + "\\n\\n\\nSave the date!\\n\\n\\n#TogetherWeCan & #TogetherWeWill";

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\"from\":\"axwaydemo.dss@gmail.com\",");
        stringBuilder.append("\"to\"");
        stringBuilder.append(":\"");
        stringBuilder.append(participant.getEmail());
        stringBuilder.append("\",");
        stringBuilder.append("\"bcc\":\"axwaydemo.dss@gmail.com\",");
        stringBuilder.append("\"subject\": \"Welcome to "+ participant.getEventName() +"!\",");
        stringBuilder.append("\"data\":\"");
        stringBuilder.append(msg);
        stringBuilder.append("\"}");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("body", stringBuilder.toString());

        String ical = "BEGIN:VCALENDAR\n" +
                "VERSION:2.0\n" +
                "PRODID:-fitogether-demo-uid-name\n" +
     //           "X-WR-CALNAME:" + participant.getEventName() + "\n" +
                "NAME:" + participant.getEventName() +"\n" +
                "CALSCALE:GREGORIAN\n" +
                "BEGIN:VEVENT\n" +
                "DTSTAMP:" + dateFormat.format(new Date(Instant.now().toEpochMilli())) + "\n" +
                "UID:fit-together-demo-uid\n" +
                "DTSTART;TZID=/Etc/UTC:" + dateFormat.format(new Date(utcStartTime.toInstant().toEpochMilli())) + "\n" +
                "DTEND;TZID=/Etc/UTC:"+ dateFormat.format(new Date(utcEndTime.toInstant().toEpochMilli())) + "\n" +
                "LOCATION:Virtual Run\n" +
                "SUMMARY:"+participant.getEventName() +"\n" +
                "DESCRIPTION:"+msg+"\n" +
                "TRANSP:TRANSPARENT\n" +
                "X-MICROSOFT-CDO-BUSYSTATUS:BUSY\n" +
                "BEGIN:VALARM\n" +
                "ACTION:DISPLAY\n" +
                "DESCRIPTION:Event Name\n" +
                "TRIGGER:-PT30M\n" +
                "END:VALARM\n" +
                "END:VEVENT\n" +
                "END:VCALENDAR";

       // map.add("file", new ByteArrayResource(ical.getBytes()));
        MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
        ContentDisposition contentDisposition = ContentDisposition
                .builder("form-data")
                .name("file")
                .filename(participant.getEventName() + ".ics")
                .build();
        fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
        HttpEntity<byte[]> fileEntity = new HttpEntity<>(ical.getBytes(), fileMap);
        map.add("file", fileEntity);
        HttpEntity requestEntity = new HttpEntity(map, headers);
        ResponseEntity<String> response = restTemplateAxway.postForEntity(emailURL,requestEntity, String.class);
        int statusCode = response.getStatusCodeValue();
        logger.info("Status code from IB : ", statusCode);
        if(statusCode == 200) {
            logger.info("Email Successfully sent to user  : {}", participant.getEmail());
        }
       return CompletableFuture.completedFuture(null);
        //return null;


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
