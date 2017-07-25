package com.hashtagplus.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "message_hashtag")
public class MessageHashtag {

    @Id
    public ObjectId id;

    @DBRef
    public Message message;

    public String hashtag;

    public MessageHashtag() {}


    public MessageHashtag(Message message, String hashtag) {
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
