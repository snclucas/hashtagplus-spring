package com.hashtagplus.model.repo;

import com.hashtagplus.model.Hashtag;
import com.hashtagplus.model.HtplUser;
import com.hashtagplus.model.HtplUserDetails;
import com.hashtagplus.model.MessageHashtag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface  MessageHashtagRepositoryCustom {
    List<AggDao> aggregate(HtplUser user);

    Page<MessageHashtag> getMessagesWithTopicAndHashtags(Hashtag topicHashtag, List<Hashtag> hashtags, Pageable pageable);
}
