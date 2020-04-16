package com.axway.runners;

import lombok.Data;

import java.util.Date;

@Data
public class Feed {
    private String senderName;
    private String timeStamp;
    private String message;
    private String activityId;
    private String athleteId;
    private float distance;
    private float duration;
    private String description;
    private String country;
    private long eventTime;
    private Date eventDateTime;
    private String type;
}
