package com.axway.runners.repo;

import com.axway.runners.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

public interface UserRepository extends ElasticsearchCrudRepository<User, String> {

    User findByEmail(String email);
}
