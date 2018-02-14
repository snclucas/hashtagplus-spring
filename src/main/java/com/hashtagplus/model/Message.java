package com.hashtagplus.model;

import com.hashtagplus.model.util.*;
import com.mongodb.annotations.Immutable;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.text.DateFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@Document(collection = "messages")
public class Message {

    @Id
    public String id;

    public String title;
    public String text;
    public String created_at;
    public String slug;

    public String user_id;

    @DBRef(lazy = true)
    public List<Hashtag> hashtags = new ArrayList<>();

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public Message() {
        this.id = new ObjectId().toString();
    }

    public Message(String title, String text, String user_id) {
        this();
        this.title = title;
        this.text = text;
        this.user_id = user_id;

        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        this.created_at = df.format(new Date());

        this.slug = toSlug(title);
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Hashtag> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<Hashtag> hashtags) {
        this.hashtags = hashtags;
    }

    public void setSlug(String text) {
        this.slug = toSlug(text);
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setCreatedOnNow() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        this.created_at = df.format(new Date());
    }

    @Override
    public String toString() {
        return String.format(
                "Message[id=%s, title='%s', text='%s']",
                id, title, text);
    }

    private static String toSlug(String input) {
        String randomPart = new RandomString(6).nextString();
        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH) + "-" + randomPart;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Message message = (Message) o;

        return id != null ? id.equals(message.id) : message.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
