package com.axway.runners;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

public class Main {

    public static void main(String[] args){

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.DATE, 19);
        calendar.set(Calendar.MILLISECOND, 1);
        //calendar.set(Calendar.AM_PM, Calendar.AM);



        System.out.println(calendar.getTimeInMillis());
        System.out.println(calendar.getTime());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        System.out.println(simpleDateFormat.format(calendar.getTime()));

        calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        calendar.set(Calendar.DATE, 19);
        calendar.add(Calendar.DATE , 7);
       // calendar.set(Calendar.AM_PM, Calendar.PM);

        System.out.println(calendar.getTimeInMillis());
        System.out.println(calendar.getTime());
        System.out.println(simpleDateFormat.format(calendar.getTime()));


       // System.out.println(new Date(Long.parseLong("1587736800001")));


        //system.out.println(UUID.randomUUID().toString());

//        calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(1586760274 * 1000);
//
//        System.out.println(calendar.getTime());
//
//        System.out.println(Instant.now().getEpochSecond());
//
//        Instant instant = Instant.ofEpochSecond(1586760274);
//        Date date = new Date();
//        date.setTime(instant.toEpochMilli());
//        System.out.println(date.toString());
        //ZonedDateTime zonedDateTime =ZonedDateTime.ofInstant(instant, ZoneOffset.UTC);
        //zonedDateTime.get
       // Instant.now().

    }

}
