package com.axway.runners.service;

import com.axway.runners.UnMatchedEventFeed;
import com.axway.runners.repo.UnMatchedEventFeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnMatchedEventFeedService {

    @Autowired
    private UnMatchedEventFeedRepository unMatchedEventFeedRepository;

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
