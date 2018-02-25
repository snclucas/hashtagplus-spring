package com.hashtagplus.model.repo;

import com.hashtagplus.model.Hashtag;
import com.hashtagplus.model.HtplUser;
import com.hashtagplus.model.Message;
import com.hashtagplus.model.MessageHashtag;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageHashtagRepository extends MongoRepository<MessageHashtag, String>, MessageHashtagRepositoryCustom {

  //@Query(value = "{'message_id': ?0}")
  List<Message> findByMessage_id(ObjectId message_id);

 // @Query(value = "{'hashtag': ?0}")
 // List<MessageHashtag> findByHashtag(Hashtag hashtag);

  @Query(value = "{'hashtag': ?0}")
  List<MessageHashtag> findByHashtagIs(Hashtag hashtag);



  List<MessageHashtag> findByMessage(Message message);

  //@Query(value = "{'$or':[ ?0 ] }")
  @Query(value="{ 'hashtag.$id' : ?0 }")
  List<MessageHashtag> findByHashtag(ObjectId hashtagId);

 // @Query(value="select o from message_hashtag o where hashtag.$id in :ids" )
 // @Query(value="{ 'hashtag.$id' : ?0 }")
  List<MessageHashtag> findByHashtagIn(@Param("ids") List<String> hashtags);

  //@Query(value="{'hashtags' : [?0], 'user_id' : ?1}")
  //@Query(value="{'hashtags' : ?0")
  //Page<MessageHashtag> findMessageHashtagsByHashtagIdIn(List<Hashtag> hashtags, Pageable pageable);


  @Override
  @Query(value = "{'message': ?0}")
  void deleteAll();

  void deleteMessageHashtagsByMessage(Message message);

}