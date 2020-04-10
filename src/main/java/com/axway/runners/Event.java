package com.axway.runners;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Document(indexName = "event")
public class Event{

    @Id
    private String id;
    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private byte[] image;
    private List<String> tags;
    private Map<String, Object> metadata;
    private List<Feed> feeds;
    private List<Participant> participants;

    public void addParticipant(Participant participant){
        if( participants == null){
            participants = new ArrayList<>();
        }
        participants.add(participant);
    }

    public void addFeed(Feed feed){
        if( feeds == null){
            feeds = new ArrayList<>();
        }
        feeds.add(feed);
    }

}