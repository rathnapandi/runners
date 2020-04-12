package com.axway.runners.service;

import com.axway.runners.Participant;
import com.axway.runners.repo.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;


    public Participant findParticipantByEventId(String id){
        return participantRepository.findParticipantByEventId();
    }

    public Participant findById(String id){
        return participantRepository.findById(id).get();
    }

    public Participant saveParticipant(Participant participant){
        return participantRepository.save(participant);
    }

    public void deleteParticipant(Participant participant){
         participantRepository.delete(participant);
    }
}
