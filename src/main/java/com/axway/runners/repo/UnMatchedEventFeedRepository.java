package com.axway.runners.repo;

import com.axway.runners.Participant;
import com.axway.runners.UnMatchedEventFeed;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

public interface UnMatchedEventFeedRepository extends ElasticsearchCrudRepository<UnMatchedEventFeed, String> {
    UnMatchedEventFeed findByActivityId(String activityId);
}
