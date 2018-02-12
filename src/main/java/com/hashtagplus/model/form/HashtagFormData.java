package com.hashtagplus.model.form;


public class HashtagFormData {

    public String text;

    public HashtagFormData() {}

    public HashtagFormData(String text) {
            this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
