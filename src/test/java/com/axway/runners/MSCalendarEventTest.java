package com.axway.runners;

//import net.fortuna.ical4j.util.MapTimeZoneCache;
//import org.junit.jupiter.api.Test;
//
//import java.util.Calendar;

import com.axway.runners.model.Participant;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

public class MSCalendarEventTest {


//    MSCalendarEvent msCalendarEvent = new MSCalendarEvent();
//
//    @Test
//    public void testIcal(){
//        System.setProperty("net.fortuna.ical4j.timezone.cache.impl", "net.fortuna.ical4j.util.MapTimeZoneCache");
//        System.setProperty("net.fortuna.ical4j.timezone.registry", "net.fortuna.ical4j.zoneinfo.outlook.OutlookTimeZoneRegistryFactory");
//
//
//        Participant participant = new Participant();
//        participant.setCountryCode("US");
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.HOUR_OF_DAY, 7);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.DATE, 24);
//        calendar.set(Calendar.MILLISECOND, 1);
//        //calendar.set(Calendar.AM_PM, Calendar.AM);
//        participant.setStartTime(calendar.getTimeInMillis() + "");
//
//
//
//
//
//        calendar = Calendar.getInstance();
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.HOUR_OF_DAY, 8);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//        calendar.set(Calendar.DATE, 24);
//        participant.setEventName("Test2");
//
//        participant.setEndTime(calendar.getTimeInMillis() + "");
//        participant.setFirstName("Rathna");
//        participant.setLastName("Natarajan");
//        participant.setEmail("rnatarajan@axway.com");
//        String ical = msCalendarEvent.createEvent(participant);
//        System.out.println(ical);
//
//    }
DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
    @Test
    public void testEmailMsg(){
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Participant participant = new Participant();

        participant.setEventName("Test event");

        participant.setFirstName("Rathna");
        participant.setLastName("Natarajan");
        participant.setEventId("dUj1I4MBHEgOW7j5D5so");

        participant.setEmail("rnatarajan@axway.com");


        participant.setCountryCode("US");
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeZone(TimeZone.getTimeZone("America/Phoenix"));
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.HOUR_OF_DAY, 7);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.DATE, 24);
        calendar1.set(Calendar.MILLISECOND, 1);
        //calendar.set(Calendar.AM_PM, Calendar.AM);
        participant.setStartTime(calendar1.getTime());





        calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.HOUR_OF_DAY, 8);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);
        calendar1.set(Calendar.DATE, 24);
        participant.setEndTime(calendar1.getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(participant.getStartTime().getTime());
        OffsetDateTime utcStartTime  = calendar.toInstant().atOffset(ZoneOffset.UTC);

        calendar.setTimeInMillis(participant.getEndTime().getTime());

        OffsetDateTime utcEndTime  = calendar.toInstant().atOffset(ZoneOffset.UTC);

        String msg = "Thank you " + participant.getFirstName() + " " + participant.getLastName() +
                " for registering with FiTogether \n\nWe are very excited you signed up with us. We are looking forward to your participation with this challenge\n\n " +
                "Important details to keep in mind for the upcoming challenge event on "
                + utcStartTime.getDayOfMonth() + "\n\nStart Time: " + utcStartTime.getHour()+ " " +utcStartTime.getMinute() +"\n\nEndTime: " + utcEndTime.getHour() + " " + utcEndTime.getMinute()
                + "\n\nEvent Name: " + participant.getEventName() + "\n\nCountry: "
                + participant.getCountryCode() + "\n\n\nSave the date!\n\n\n#TogetherWeCan & #TogetherWeWill";

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\"from\":\"axwaydemo.dss@gmail.com\",");
        stringBuilder.append("\"to\"");
        stringBuilder.append(":\"");
        stringBuilder.append(participant.getEmail());
        stringBuilder.append("\",");
        stringBuilder.append("\"bcc\":\"axwaydemo.dss@gmail.com\",");
        stringBuilder.append("\"subject\": \"Welcome to \"+ eventName +\"!\",");
        stringBuilder.append("\"data\":\"");
        stringBuilder.append(msg);
        stringBuilder.append("\"}");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        Map<String, Object> map = new HashMap<>();
        map.put("body", stringBuilder.toString());

        String ical = "BEGIN:VCALENDAR\n" +
                "VERSION:2.0\n" +
                "PRODID:-fitogether-demo-uid-name\n" +
                "X-WR-CALNAME:" + participant.getEventName() + "\n" +
                "NAME:" + participant.getEventName() +"\n" +
                "CALSCALE:GREGORIAN\n" +
                "BEGIN:VEVENT\n" +
                "DTSTAMP:" + dateFormat.format(new Date(Instant.now().toEpochMilli())) + "\n" +
                "UID:fit-together-demo-uid\n" +
                "DTSTART;TZID=/Etc/UTC:" + dateFormat.format(new Date(utcStartTime.toInstant().toEpochMilli())) + "\n" +
                "DTEND;TZID=/Etc/UTC:"+ dateFormat.format(new Date(utcEndTime.toInstant().toEpochMilli())) + "\n" +
                "LOCATION=Virtual Run\n" +
                "SUMMARY:Event Name\n" +
                "DESCRIPTION:Event Description\n" +
                "TRANSP:TRANSPARENT\n" +
                "X-MICROSOFT-CDO-BUSYSTATUS:BUSY\n" +
                "BEGIN:VALARM\n" +
                "ACTION:DISPLAY\n" +
                "DESCRIPTION:Event Name\n" +
                "TRIGGER:-PT30M\n" +
                "END:VALARM\n" +
                "END:VEVENT\n" +
                "END:VCALENDAR";

        System.out.println(ical);
        System.out.println(utcStartTime.getHour());

    }
}
