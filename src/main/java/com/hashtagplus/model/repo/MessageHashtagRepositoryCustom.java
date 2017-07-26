package com.hashtagplus.model.repo;

import com.hashtagplus.model.HtplUserDetails;
import com.hashtagplus.model.MessageHashtag;

import java.util.List;


public interface  MessageHashtagRepositoryCustom {
    List<AggDao> aggregate(HtplUserDetails user);
}
