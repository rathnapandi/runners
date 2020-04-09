package com.axway.runners.strava;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class OAuthToken {
    private String token_type;
    private long expires_at;
    private long expires_in;
    private String access_token;
    private String refresh_token;

}
