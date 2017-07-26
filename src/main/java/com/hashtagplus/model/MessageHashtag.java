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

    public String user_id;

    public String hashtag;

    public MessageHashtag() {
        this.id = new ObjectId().toString();
    }


    public MessageHashtag(Message message, String hashtag, String user_id) {
        this();
        this.message = message;
        this.hashtag = hashtag;
        this.user_id = user_id;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
