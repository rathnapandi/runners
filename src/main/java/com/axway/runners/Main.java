package com.axway.runners;

import java.util.Calendar;
import java.util.Date;

public class Main {

    public static void main(String[] args){

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 5);
        System.out.println(calendar.getTimeInMillis());

        System.out.println(System.currentTimeMillis());
    }
}
