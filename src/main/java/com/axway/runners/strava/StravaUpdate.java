package com.axway.runners.strava;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StravaUpdate {

    private String title;
    private String type;
    @JsonProperty("private")
    private boolean visibility;
}
