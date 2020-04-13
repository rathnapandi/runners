package com.axway.runners;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Main {

    public static void main(String[] args){

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 5);
        System.out.println(calendar.getTimeInMillis());

        System.out.println(System.currentTimeMillis());
        System.out.println(UUID.randomUUID().toString());

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(1586760274 * 1000);

        System.out.println(calendar.getTime());

        System.out.println(Instant.now().getEpochSecond());

        Instant instant = Instant.ofEpochSecond(1586760274);
        Date date = new Date();
        date.setTime(instant.toEpochMilli());
        System.out.println(date.toString());
        //ZonedDateTime zonedDateTime =ZonedDateTime.ofInstant(instant, ZoneOffset.UTC);
        //zonedDateTime.get
       // Instant.now().

    }
}
