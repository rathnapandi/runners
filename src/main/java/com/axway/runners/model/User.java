package com.axway.runners.model;

import com.axway.runners.strava.OAuthToken;
import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Map;

@Data
@Document(indexName = "user")

public class User {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String countryCode;
    @Field(type = FieldType.Nested, includeInParent = true)
    private OAuthToken oAuthToken;
    private Map<String, Object> metadata;
    private String athleteId;
    @Version
    private Long version;
}
