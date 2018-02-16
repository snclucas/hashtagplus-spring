package com.hashtagplus.model.repo;

import java.util.List;

import com.hashtagplus.model.Message;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface MessageRepository extends MongoRepository<Message, String> {

  Message findByTitle(String title);

  @Query(value = "{hashtags: ?0}")
  List<Message> findByHashtag(String hashtag);

  @Query(value = "{slug: ?0}")
  Message findOneBySlug(String slug);

  @Query(value = "{user_id: ?0}")
  List<Message> findAll(String user_id, Pageable pageable);

  @Query(value = "{user_id: ?0, hashtags: ?1}")
  List<Message> findByUserIdAndHashtags(String user_id, String hashtag);

  @Query(value = "{hashtags: ?0}")
  List<Message> findAllByHashtag(String user_id);

  //List<Message> findByUserInAndHashtagIn(List<String> emails, List<String> pinCodes);
}