package com.axway.runners.model;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

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
    @Field(type= FieldType.Date)
    private Date eventTime;
    @Field(type= FieldType.Date)
    private Date eventDateTime;
    private String type;
}
