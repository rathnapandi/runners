package com.axway.runners;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "unmatchedeventfeed", type = "unmatchedeventfeed")
public class UnMatchedEventFeed extends Feed{
    @Id
    private String id;
    @Version
    private Long version;
}
