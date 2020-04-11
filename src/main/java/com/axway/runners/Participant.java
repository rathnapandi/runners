package com.axway.runners;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Participant {
    private String email;
    private String firstName;
    private String lastName;
    private long startTime;
    private long endTime;
    private String note;
    
}
