package com.hashtagplus.model.repo;


import com.hashtagplus.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

public class MessageHashtagRepositoryImpl implements MessageHashtagRepositoryCustom {


  private final MongoTemplate mongoTemplate;

  @Autowired
  public MessageHashtagRepositoryImpl(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public List<AggDao> aggregate(HtplUser user) {
    GroupOperation groupOperation = getGroupOperation();

    SortOperation sorter = sort(new Sort(Sort.Direction.DESC, "count")).and(Sort.Direction.ASC, "hashtag");

    Aggregation agg = Aggregation.newAggregation(getMatchOperation(user), groupOperation, sorter);
    AggregationResults<AggDao> output
            = mongoTemplate.aggregate(agg, "message_hashtag", AggDao.class);
    return output.getMappedResults();
  }

  private MatchOperation getMatchOperation(HtplUser user) {
    String user_id = user.getId();
    Criteria userCriteria = where("username").is(user.getUsername());
    return match(userCriteria);
  }

  private GroupOperation getGroupOperation() {
    return group("hashtag")
            .count().as("count");
  }

  public Page<MessageHashtag> getMessagesWithTopicAndHashtags(Hashtag topicHashtag, List<Hashtag> hashtags, Pageable pageable) {
    List<Criteria> orCriterias = new ArrayList<>(hashtags.size());
    Criteria topicCriteria = Criteria.where("hashtag.id").is(topicHashtag.id);

    for (Hashtag hashtag : hashtags) {
      orCriterias.add(Criteria.where("hashtag.id").is(hashtag.id));
    }
    Criteria orCriteria;
    if(orCriterias.size() > 0) {
      orCriteria = new Criteria().orOperator(orCriterias.toArray(new Criteria[hashtags.size()]));
    } else {
      orCriteria = new Criteria();
    }

    Query topicQuery = new Query().addCriteria(topicCriteria).with(pageable);
    List<MessageHashtag> withTopic = mongoTemplate.find(topicQuery, MessageHashtag.class);

    Query hashtagQuery = new Query().addCriteria(orCriteria);
    List<MessageHashtag> withHashtags = mongoTemplate.find(hashtagQuery, MessageHashtag.class);

    List<MessageHashtag> combined = withTopic.stream().filter(withHashtags::contains).collect(Collectors.toList());

    int start = pageable.getOffset();
    int end = (start + pageable.getPageSize()) > combined.size() ? combined.size() : (start + pageable.getPageSize());
    return new PageImpl<>(combined.subList(start, end), pageable, combined.size());
  }


  public Page<MessageHashtag> simon(List<Hashtag> hashtags, HtplUser user, Pageable pageable, String privacy) {
    Criteria criteria = Criteria.where("hashtag.id").in(hashtags);
    Query query = new Query().addCriteria(criteria).with(pageable);
    List<MessageHashtag> withHashtags = mongoTemplate.find(query, MessageHashtag.class);

    // message can be null if original message was deleted but the MEssageHashtag was not
    List<MessageHashtag> filtered = withHashtags.stream()
            .filter(mh -> mh.getMessage() != null &&
                    (mh.getMessage().getPrivacy().equalsIgnoreCase(privacy) ||
                            mh.getUsername().equals(user.getId())))
            .collect(Collectors.toList());
    int start = pageable.getOffset();
    int end = (start + pageable.getPageSize()) > filtered.size() ? filtered.size() : (start + pageable.getPageSize());
    return new PageImpl<>(filtered.subList(start, end), pageable, filtered.size());
  }

}
