package com.hashtagplus.model.repo;

import com.hashtagplus.model.Hashtag;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface HashtagRepository extends MongoRepository<Hashtag, String> {

    @Query(value = "{'text': ?0}")
    Hashtag findByHashtag(String text);


}