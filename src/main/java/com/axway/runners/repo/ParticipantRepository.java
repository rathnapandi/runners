package com.axway.runners.repo;

import com.axway.runners.Participant;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

public interface ParticipantRepository extends ElasticsearchCrudRepository<Participant, String> {
    public Participant findParticipantByEventId();
}
