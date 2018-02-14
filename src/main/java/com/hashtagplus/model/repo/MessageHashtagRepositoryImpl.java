package com.hashtagplus.model.repo;


import com.hashtagplus.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.*;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

public class MessageHashtagRepositoryImpl implements MessageHashtagRepositoryCustom  {


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
        Criteria userCriteria = where("user_id").is(user_id);
        return match(userCriteria);
    }

    private GroupOperation getGroupOperation() {
        return group("hashtag")
                .count().as("count");
    }

    private void que(List<Hashtag> hashtags) {
      Query query = new Query();
      Criteria criteria = Criteria.where("");
      for(Hashtag hashtag: hashtags) {
        //criteria.andOperator(new Criteria.where("").is())
      }
      query.addCriteria(Criteria.where("name").regex("c$"));
     // List<User> users = mongoTemplate.find(query, User.class);
    }

}

//  Criteria criteria = Criteria.where("contentType").is("application/vnd.sometype");
//
//  List<Criteria> docCriterias = new ArrayList<Criteria>(docs.size());
//
//for (Document doc: docs) {
//        docCriterias.add(Criteria.where("metadata.name").is(doc.getName())
//        .and("metadata.version").is(doc.getVersion()));
//        }
//
//        criteria = criteria.orOperator(docCriterias.toArray(new Criteria[docs.size()]));