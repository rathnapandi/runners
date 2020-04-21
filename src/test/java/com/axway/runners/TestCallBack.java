package com.axway.runners;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

public class TestCallBack {

    @Test
    public void testEventDateRangeMatch(){
        Participant mockParticipant = new Participant();
        long currentTime = System.currentTimeMillis();

        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DATE, -1);

        mockParticipant.setStartTime(calendar.getTimeInMillis() + "");

        //calendar.set(Calendar.AM_PM, Calendar.AM);

        System.out.println("start time : "+ calendar.getTime());
        calendar.add(Calendar.DATE, 2);

        mockParticipant.setEndTime(calendar.getTimeInMillis() + "");

        List<Participant> participants = new ArrayList<>();
        participants.add(mockParticipant);

        System.out.println("end time : "+ calendar.getTime());

        Optional<Participant> matchedParticipant = participants.parallelStream().filter(participant -> Long.parseLong(participant.getStartTime())
                <= currentTime && Long.parseLong(participant.getEndTime()) >= currentTime).findFirst();
        System.out.println(matchedParticipant);
        if(matchedParticipant.isPresent()){
            System.out.println(matchedParticipant.get());
        }
    }
}
