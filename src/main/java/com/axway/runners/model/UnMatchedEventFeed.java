package com.axway.runners.model;

import com.axway.runners.model.Feed;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "unmatchedeventfeed")
public class UnMatchedEventFeed extends Feed {
    @Id
    private String id;
    @Version
    private Long version;
}
