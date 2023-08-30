package com.axway.runners.service;

import com.axway.runners.model.UnMatchedEventFeed;
import com.axway.runners.repo.UnMatchedEventFeedRepository;
import org.springframework.stereotype.Service;

@Service
public class UnMatchedEventFeedService {

    private final UnMatchedEventFeedRepository unMatchedEventFeedRepository;

    public UnMatchedEventFeedService(UnMatchedEventFeedRepository unMatchedEventFeedRepository){
        this.unMatchedEventFeedRepository = unMatchedEventFeedRepository;
    }

    public UnMatchedEventFeed save(UnMatchedEventFeed unMatchedEventFeed){
        return unMatchedEventFeedRepository.save(unMatchedEventFeed);
    }

    public UnMatchedEventFeed findByActivityId(String activityId){
        return  unMatchedEventFeedRepository.findByActivityId(activityId);
    }

    public void delete(UnMatchedEventFeed unMatchedEventFeed){
       unMatchedEventFeedRepository.delete(unMatchedEventFeed);
    }

    public void deleteByActivityId(String activityId){
        unMatchedEventFeedRepository.deleteByActivityId(activityId);
    }

    public void deleteById(String id){
        unMatchedEventFeedRepository.deleteById(id);
    }
}
