package com.axway.runners.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.elasticsearch.annotations.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(indexName = "unmatchedeventfeedv2")
public class UnMatchedEventFeed extends Feed {
    @Id
    private String id;
    @Version
    private Long version;
}
