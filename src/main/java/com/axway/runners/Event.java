package com.axway.runners;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Document(indexName = "event")
public class Event{

    @Id
    private String id;
    private String name;
    private String description;
    private long startDate;
    private long endDate;


    private byte[] image;
    private List<String> tags;
    private Map<String, Object> metadata;
    private List<Feed> feeds;
    private List<Participant> participants;

    @Version
    private Long version;

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