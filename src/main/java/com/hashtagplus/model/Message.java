package com.hashtagplus.model;

import com.mongodb.annotations.Immutable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Immutable
@Document(collection = "messages")
public class Message {

    @Id
    public String id;

    public String title;
    public String text;
    public String created_at;

    public List<String> hashtags;

    public Message() {}

    public Message(String title, String text, List<String> hashtags) {
        this.title = title;
        this.text = text;
        this.hashtags = hashtags;

        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        String nowAsISO = df.format(new Date());
        this.created_at = nowAsISO;
    }

    @Override
    public String toString() {
        return String.format(
                "Message[id=%s, title='%s', text='%s']",
                id, title, text);
    }

}
