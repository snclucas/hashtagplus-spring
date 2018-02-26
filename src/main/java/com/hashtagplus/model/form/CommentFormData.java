package com.hashtagplus.model.form;


public class CommentFormData {

  public String parent;

  public String text;

  public CommentFormData() {}

  public CommentFormData(String text, String parent) {
    this.text = text;
    this.parent = parent;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getParent() {
    return parent;
  }

  public void setParent(String parent) {
    this.parent = parent;
  }
}
