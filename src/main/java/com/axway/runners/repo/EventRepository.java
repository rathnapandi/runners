package com.axway.runners.repo;

import com.axway.runners.Event;
import com.axway.runners.Feed;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends ElasticsearchCrudRepository<Event, String>{
   // public Event findByParticipants(String email);

    @Query("{\"bool\": {\"must\": [{\"match\": {\"feeds.activityId\": \"?0\"}}]}}")
    Event findByFeedsUsingCustomQuery(String activityId);
}
