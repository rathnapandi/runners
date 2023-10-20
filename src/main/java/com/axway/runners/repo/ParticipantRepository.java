package com.axway.runners.repo;

import com.axway.runners.model.Participant;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ParticipantRepository extends ElasticsearchRepository<Participant, String> {
    List<Participant> findByEmail(String email);

    List<Participant> findParticipantsByEventId(String eventId);

    List<Participant> findParticipantsByEmailAndEventId(String email, String eventId);

    @Query("select c from Contact c " +
        "where lower(c.firstName) like lower(concat('%', :searchTerm, '%')) " +
        "or lower(c.lastName) like lower(concat('%', :searchTerm, '%'))")
    List<Participant> search(@Param("searchTerm") String searchTerm);
}
