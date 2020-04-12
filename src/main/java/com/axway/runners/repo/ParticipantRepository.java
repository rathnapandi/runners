package com.axway.runners.repo;

import com.axway.runners.Participant;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

import java.util.List;

public interface ParticipantRepository extends ElasticsearchCrudRepository<Participant, String> {
    public Participant findParticipantByEventId();
    public List<Participant> findParticipantsByEventId(String eventId);
}
