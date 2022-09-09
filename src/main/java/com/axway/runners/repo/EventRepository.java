package com.axway.runners.repo;

import com.axway.runners.Event;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends ElasticsearchRepository<Event, String> {
   // public Event findByParticipants(String email);

    @Query("{\"bool\": {\"must\": [{\"match\": {\"feeds.activityId\": \"?0\"}}]}}")
    Event findByFeedsUsingCustomQuery(String activityId);
}
