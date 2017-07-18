package com.hashtagplus.model;

import org.springframework.data.annotation.Id;

public class Message {

    @Id
    public String id;

    public String title;
    public String description;

    public Message() {}

    public Message(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format(
                "Message[id=%s, title='%s', description='%s']",
                id, title, description);
    }

}
