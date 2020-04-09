package com.axway.runners;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;
import java.util.Map;

@Data
@Document(indexName = "event")
public class Event{

    @Id
    private String id;
    private String name;
    private String description;
    private byte[] image;
    private List<String> tags;
    private Map<String, Object> metadata;
    private List<Feed> feeds;
    private List<Participant> participants;

}