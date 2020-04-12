package com.axway.runners.repo;

import com.axway.runners.Event;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends ElasticsearchCrudRepository<Event, String>{
   // public Event findByParticipants(String email);
}
