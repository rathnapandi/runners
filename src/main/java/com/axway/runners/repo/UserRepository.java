package com.axway.runners.repo;

import com.axway.runners.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserRepository extends ElasticsearchRepository<User, String> {

    User findByEmail(String email);
    User findByAthleteId(String athleteId);

}
