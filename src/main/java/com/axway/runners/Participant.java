package com.axway.runners;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(indexName = "participant")
public class Participant {
    @Id
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String eventName;
    private String eventId;
    private String countryCode;
    private String startTime;
    private String endTime;
    private String note;
    private String type;
    @Version
    private Long version;
    
}
