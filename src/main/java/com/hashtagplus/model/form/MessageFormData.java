package com.hashtagplus.model.form;


public class MessageFormData {

    public String title;

    public String text;

    public String hashtags;

    public MessageFormData() {}

    public MessageFormData(String title, String text, String hashtags) {
            this.title = title;
            this.text = text;
            this.hashtags = hashtags;
        }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHashtags() {
        return hashtags;
    }

    public void setHashtags(String hashtags) {
        this.hashtags = hashtags;
    }

    public String getTitle() {
        return title;

    }

    public void setTitle(String title) {
        this.title = title;
    }
}
