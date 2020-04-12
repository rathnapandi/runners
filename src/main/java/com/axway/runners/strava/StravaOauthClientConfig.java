package com.axway.runners.strava;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "strava")
@Configuration
@Data
public class StravaOauthClientConfig {

    private String client_id;
    private String client_secret;
    private String token_uri;
    private String authorization_uri;
    private String scope;
    private String grant_type;
    private String redirect_uri;
    private String callback_url;
}
