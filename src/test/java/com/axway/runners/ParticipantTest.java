package com.axway.runners;

import com.axway.runners.model.Participant;
import com.axway.runners.service.ParticipantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;

@SpringBootTest
public class ParticipantTest {

    @Autowired
    private ParticipantService participantService;

    @Test
    public void addParticipant(){

        Calendar calendar = Calendar.getInstance();

        Participant participant = new Participant();
        participant.setFirstName("Rathna");
        participant.setLastName("Natarajan");
        participant.setCountryCode("US");
        participant.setEventId("dUj1I4MBHEgOW7j5D5so");
        participant.setStartTime(calendar.getTime());
        calendar.add(Calendar.MINUTE, 30);
        participant.setEndTime(calendar.getTime());
        participantService.saveParticipant(participant);
    }

}
