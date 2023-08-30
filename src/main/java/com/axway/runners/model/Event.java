package com.axway.runners.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Document(indexName = "eventv2")
public class Event {

    @Id
    private String id;
    private String name;
    private String description;
    @Field(type = FieldType.Date)
    private Date startDate;
    @Field(type = FieldType.Date)
    private Date endDate;


    private String image;
    private List<String> tags;
    private Map<String, Object> metadata;
    private List<Feed> feeds;

    @Version
    private Long version;

    public void addFeed(Feed feed) {
        if (feeds == null) {
            feeds = new ArrayList<>();
        }
        feeds.add(feed);
    }

}
