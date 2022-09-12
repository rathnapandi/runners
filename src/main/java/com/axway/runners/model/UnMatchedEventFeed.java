package com.axway.runners.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "unmatchedeventfeedv2")
public class UnMatchedEventFeed extends Feed {
    @Id
    private String id;
    @Version
    private Long version;
}
