package com.hashtagplus.model.form;


public class MessageFormData {

  public String title;

  public String text;

  public MessageFormData() {}

  public MessageFormData(String title, String text) {
    this.title = title;
    this.text = text;
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
}
