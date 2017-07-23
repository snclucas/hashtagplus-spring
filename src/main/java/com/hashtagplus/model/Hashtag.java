package com.hashtagplus.model;

import com.mongodb.annotations.Immutable;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "hashtags")
public class Hashtag {

    @Id
    public ObjectId id;

    public String text;

    public Hashtag() {}

    public Hashtag(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return String.format(
                "Message[id=%s, text='%s']",
                id, text);
    }

}
