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

    public String hashtag;

    public MessageHashtag() {
        this.id = new ObjectId().toString();
    }


    public MessageHashtag(Message message, String hashtag) {
        this();
        this.message = message;
        this.hashtag = hashtag;
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
}
