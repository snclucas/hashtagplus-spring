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

    public String username;

    @DBRef
    public Hashtag hashtag;

    public MessageHashtag() {
        this.id = new ObjectId().toString();
    }


    public MessageHashtag(Message message, Hashtag hashtag, String username) {
        this();
        this.message = message;
        this.hashtag = hashtag;
        this.username = username;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final MessageHashtag that = (MessageHashtag) o;

    return getMessage() != null ? getMessage().equals(that.getMessage()) : that.getMessage() == null;
  }

  @Override
  public int hashCode() {
    return getMessage() != null ? getMessage().hashCode() : 0;
  }
}
