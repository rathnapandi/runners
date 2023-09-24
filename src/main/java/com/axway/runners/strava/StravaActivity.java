package com.axway.runners.strava;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)

public class StravaActivity {
    private long id;
    private String name;
    private double distance;
    private String type;
    private int moving_time;
    private String location_country;
    private String start_date;

}
