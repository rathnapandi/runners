package com.axway.runners;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

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

    private Map<String, Object> metadata;
}
