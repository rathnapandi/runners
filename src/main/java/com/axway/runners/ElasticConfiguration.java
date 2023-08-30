package com.axway.runners;

import com.axway.runners.strava.OAuthToken;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration

@EnableElasticsearchRepositories(basePackages = "com.axway.runners.repo")
//@ComponentScan(basePackages = { "com.axway.runners.service" })
public class ElasticConfiguration extends  AbstractElasticsearchConfiguration {
    @Value("${elasticsearch.host}")
    private String elasticsearchHost;

    @Value("${elasticsearch.username}")
    private String username;

    @Value("${elasticsearch.password}")
    private String password;

    @Autowired
    private Environment environment;

    @Override
    public RestHighLevelClient elasticsearchClient() {

        String profile = environment.getProperty("spring.profiles.active");

        ClientConfiguration clientConfiguration = null;
        if( profile.equals("dev")){
            clientConfiguration = ClientConfiguration.builder().connectedTo(elasticsearchHost).usingSsl().withBasicAuth(username, password).build();
        }else{
            clientConfiguration = ClientConfiguration.builder().connectedTo(elasticsearchHost).build();
        }

        return RestClients.create(clientConfiguration).rest();
    }

//    @Bean
//    public ElasticsearchOperations elasticsearchTemplate() {
//        return new ElasticsearchRestTemplate(elasticsearchClient());
//    }


//    @Bean
//    @Override
//    public EntityMapper entityMapper() {
//
//        ElasticsearchEntityMapper entityMapper = new ElasticsearchEntityMapper(
//                elasticsearchMappingContext(), new DefaultConversionService());
//        entityMapper.setConversions(elasticsearchCustomConversions());
//
//        return entityMapper;
//    }

    @Bean
    @Override
    public ElasticsearchCustomConversions elasticsearchCustomConversions() {
        return new ElasticsearchCustomConversions(
                Arrays.asList(new OAuthTokenToMap(), new MapToOAuthToken()));
    }

    @WritingConverter
    static class OAuthTokenToMap implements Converter<OAuthToken, Map<String, Object>> {

        @Override
        public Map<String, Object> convert(OAuthToken source) {

            LinkedHashMap<String, Object> target = new LinkedHashMap<>();
            target.put("accessToken", source.getAccess_token());
            target.put("refreshToken", source.getRefresh_token());
            target.put("exp", source.getExpires_at());


            return target;
        }
    }


    @ReadingConverter
    static class MapToOAuthToken implements Converter<Map<String, Object>, OAuthToken> {

        @Override
        public OAuthToken convert(Map<String, Object> source) {
            OAuthToken oAuthToken = new OAuthToken();
            String accessToken = (String) source.get("accessToken");
            String refreshToken = (String) source.get("refreshToken");
            Integer exp = (Integer) source.get("exp");

           oAuthToken.setAccess_token(accessToken);
           oAuthToken.setRefresh_token(refreshToken);
           oAuthToken.setExpires_at(exp.longValue());
           return oAuthToken;
        }
    }


//    @WritingConverter
//    static class ParticipantToMap implements Converter<List<Participant>, Map<String, Object>> {
//
//
//        @Override
//        public Map<String, Object> convert(List<Participant> participants) {
//            for (Participant participant:participants
//                 ) {
//
//            }
//        }
//    }
//
//
//    @ReadingConverter
//    static class MapToParticipant implements Converter<Map<String, Object>, OAuthToken> {
//
//        @Override
//        public OAuthToken convert(Map<String, Object> source) {
//            OAuthToken oAuthToken = new OAuthToken();
//            String accessToken = (String) source.get("accessToken");
//            String refreshToken = (String) source.get("refreshToken");
//            Integer exp = (Integer) source.get("exp");
//
//            oAuthToken.setAccess_token(accessToken);
//            oAuthToken.setRefresh_token(refreshToken);
//            oAuthToken.setExpires_at(exp.longValue());
//            return oAuthToken;
//        }
//    }


}
