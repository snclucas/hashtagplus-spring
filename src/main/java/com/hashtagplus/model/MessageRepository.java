package com.hashtagplus.model;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, String> {

    public Message findByTitle(String title);

}