package com.axway.runners.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(indexName = "participantv2")
public class Participant {
    @Id
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String eventName;
    private String eventId;
    private String countryCode;
    private Date startTime;
    private Date endTime;
    private String note;
    private String type;
    @Version
    private Long version;

}
