package com.axway.runners.repo;

import com.axway.runners.model.UnMatchedEventFeed;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UnMatchedEventFeedRepository extends ElasticsearchRepository<UnMatchedEventFeed, String> {
    UnMatchedEventFeed findByActivityId(String activityId);
    void deleteByActivityId(String activityId);
    void deleteById(String id);

}
