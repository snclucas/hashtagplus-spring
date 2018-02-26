package com.hashtagplus.model.form;


public class MessageFormData {

  public String title;
  public String text;
  public String topic = "";

  public String hashtags = "";

  public MessageFormData() {}

  public MessageFormData(String title, String text, String topic) {
    this.title = title;
    this.text = text;
    this.topic = topic;
    this.hashtags = hashtags;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getTitle() {
    return title;

  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(final String topic) {
    this.topic = topic;
  }

  public String getHashtags() {
    return hashtags;
  }

  public void setHashtags(String hashtags) {
    this.hashtags = hashtags;
  }
}
