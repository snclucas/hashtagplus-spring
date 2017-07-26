package com.hashtagplus.model.repo;


import org.springframework.data.annotation.Id;

public class AggDao {

    @Id
    private String hashtag;
    private Integer count;

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
