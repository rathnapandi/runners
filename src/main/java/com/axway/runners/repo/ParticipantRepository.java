package com.axway.runners.repo;

import com.axway.runners.Participant;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ParticipantRepository extends ElasticsearchRepository<Participant, String> {
    //public Participant findParticipantByEventId();
    List<Participant> findByEmail(String email);
    List<Participant> findParticipantsByEventId(String eventId);
    //Participant findByEmailAndEventId(String email, String eventId);
}
