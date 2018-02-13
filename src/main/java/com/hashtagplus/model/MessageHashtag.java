package com.hashtagplus.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "message_hashtag")
public class MessageHashtag {

    @Id
    public String id;

    @DBRef
    public Message message;

    public String userid;

    @DBRef
    public Hashtag hashtag;

    public MessageHashtag() {
        this.id = new ObjectId().toString();
    }


    public MessageHashtag(Message message, Hashtag hashtag, String userid) {
        this();
        this.message = message;
        this.hashtag = hashtag;
        this.userid = userid;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Hashtag getHashtag() {
        return hashtag;
    }

    public void setHashtag(Hashtag hashtag) {
        this.hashtag = hashtag;
    }

    public String getUserid() {
        return userid;
    }

    public void setUser_id(String userid) {
        this.userid = userid;
    }
}
