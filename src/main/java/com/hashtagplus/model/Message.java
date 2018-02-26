package com.hashtagplus.model;

import com.hashtagplus.model.util.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.userdetails.User;

import java.text.DateFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@Document(collection = "messages")
public class Message {

  @Id
  public String id;

  public String title = "";
  public String text = "";
  public String created_at;
  public String slug;

  public String privacy;

  public String hasText;

  public String contentType;

  public String username;

  public int score;

  @DBRef(lazy = true)
  public List<Hashtag> hashtags = new ArrayList<>();

  @DBRef(lazy = true)
  public List<Message> comments = new ArrayList<>();

  public List<MediaUrl> mediaUrls = new ArrayList<>();

  //@DBRef
  public String parent = "";;

  private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
  private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

  public Message() {
    this.id = new ObjectId().toString();
  }

  public Message(String title, String text, String username) {
    this();
    this.title = title;
    this.text = text;
    this.username = username;
    this.hasText = "true";

    this.score = 1;
    this.contentType = "text/";

    this.privacy = "public";

    TimeZone tz = TimeZone.getTimeZone("UTC");
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
    df.setTimeZone(tz);
    this.created_at = df.format(new Date());

    this.slug = toSlug(title);
  }

  public void addMediaUrl(String url, String contentType) {
    mediaUrls.add(new MediaUrl(url, contentType));
  }

  public void addMediaUrl(MediaUrl mediaUrl) {
    mediaUrls.add(mediaUrl);
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

  public String hasText() {
    return hasText;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(final String contentType) {
    this.contentType = contentType;
  }

  public int getScore() {
    return score;
  }

  public void setScore(final int score) {
    this.score = score;
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

  public String getPrivacy() {
    return privacy;
  }

  public void setPrivacy(final String privacy) {
    this.privacy = privacy;
  }

  public String getUser() {
    return username;
  }

  public Boolean hasImage() {
    return mediaUrls.stream().anyMatch(mu -> mu.contentType.startsWith("image/"));
  }

  public Boolean isImage() {
    return contentType.startsWith("image/");
  }

  public Boolean hasVideo() {
    return mediaUrls.stream().anyMatch(mu -> mu.contentType.startsWith("video/"));
  }

  public Boolean isVideo() {
    return contentType.startsWith("video/");
  }

  public Boolean hasAudio() {
    return mediaUrls.stream().anyMatch(mu -> mu.contentType.startsWith("audio/"));
  }

  public Boolean isAudio() {
    return contentType.startsWith("audio/");
  }

  public Boolean hasMedia() {
    return mediaUrls
            .stream()
            .anyMatch(mu -> mu.contentType.startsWith("audio/") ||
            mu.contentType.startsWith("video/") ||
            mu.contentType.startsWith("image/"));
  }

  public void setUser(String username) {
    this.username = username;
  }

  public List<Message> getComments() {
    return comments;
  }

  public void addComment(Message comment) {
    this.comments.add(comment);
  }


  public String getParent() {
    return parent;
  }

  public void setParent(final String parent) {
    this.parent = parent;
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
    if(input == null || input.equals("")) {
      return randomPart;
    } else {
      String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
      String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
      String slug = NONLATIN.matcher(normalized).replaceAll("");
      return slug.toLowerCase(Locale.ENGLISH) + "-" + randomPart;
    }
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
