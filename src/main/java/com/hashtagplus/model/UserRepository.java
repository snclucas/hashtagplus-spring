package com.hashtagplus.model;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserRepository extends MongoRepository<User, String> {

    @Query(value = "{'username': ?0}")
    public User findByUsername(String userName);

}