package com.axway.runners.repo;

import com.axway.runners.Participant;
import com.axway.runners.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

import java.util.List;

public interface ParticipantRepository extends ElasticsearchCrudRepository<Participant, String> {
    //public Participant findParticipantByEventId();
    List<Participant> findByEmail(String email);
    List<Participant> findParticipantsByEventId(String eventId);
    //Participant findByEmailAndEventId(String email, String eventId);
}
