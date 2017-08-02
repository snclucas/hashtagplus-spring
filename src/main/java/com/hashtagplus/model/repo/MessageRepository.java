package com.hashtagplus.model.repo;

import java.util.List;

import com.hashtagplus.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface MessageRepository extends MongoRepository<Message, String> {

    Message findByTitle(String title);

    @Query(value = "{hashtags: ?0}")
    List<Message> findByHashtag(String hashtag);

    @Query(value = "{user_id: ?0}")
    List<Message> findAll(String user_id);
}