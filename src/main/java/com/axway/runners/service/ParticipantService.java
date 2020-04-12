package com.axway.runners.service;

import com.axway.runners.Participant;
import com.axway.runners.repo.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;


//    public Participant findParticipantByEvent(String id){
//        return participantRepository.findParticipantByEventId();
//    }

    public Participant findById(String id){
        return participantRepository.findById(id).get();
    }

    public List<Participant> findParticipantsByEventId(String eventId){
        return participantRepository.findParticipantsByEventId(eventId);
    }

    public void deleteAll(){
        participantRepository.deleteAll();
    }

    public Participant saveParticipant(Participant participant){
        return participantRepository.save(participant);
    }

    public void deleteParticipant(Participant participant){
         participantRepository.delete(participant);
    }
}
