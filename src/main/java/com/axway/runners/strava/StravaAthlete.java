package com.axway.runners.strava;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StravaAthlete {

    private String aspect_type;
    private long event_time;
    private long object_id;
    private String object_type;
    private long owner_id;
    private long subscription_id;
    private StravaAthlete updates;
    private String title;

}
