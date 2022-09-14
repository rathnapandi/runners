package com.axway.runners.service;

import com.axway.runners.model.Participant;
import com.axway.runners.repo.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;

    public Participant findById(String id){
        return participantRepository.findById(id).get();
    }

    public List<Participant> findByEmail(String email) {
        return participantRepository.findByEmail(email);
    }

    public List<Participant> findAllParticipants(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            Iterable<Participant> participants = participantRepository.findAll();
            return StreamSupport.stream(participants.spliterator(), false)
                    .collect(Collectors.toList());
        } else {
            return participantRepository.search(stringFilter);
        }
    }

    public List<Participant> findParticipantsByEventId(String eventId){
        return participantRepository.findParticipantsByEventId(eventId);
    }

    public List<Participant> findParticipantsByEmailAAndEventId(String email, String eventId){
        return participantRepository.findParticipantsByEmailAndEventId(email, eventId);
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
