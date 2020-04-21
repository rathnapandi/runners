package com.axway.runners.service;

import com.axway.runners.UnMatchedEventFeed;
import com.axway.runners.repo.UnMatchedEventFeedRepository;
import org.springframework.stereotype.Service;

@Service
public class UnMatchedEventFeedService {

    private UnMatchedEventFeedRepository unMatchedEventFeedRepository;

    public UnMatchedEventFeed save(UnMatchedEventFeed unMatchedEventFeed){
        return unMatchedEventFeedRepository.save(unMatchedEventFeed);
    }
}
