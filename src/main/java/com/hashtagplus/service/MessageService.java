package com.hashtagplus.service;

import com.hashtagplus.model.Message;
import com.hashtagplus.model.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Value("${name:World}")
    private String name;

    public String getHelloMessage() {
        return "Hello " + this.name;
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(String id) {
        return messageRepository.findOne(id);
    }

    public Message saveMessage(String title, String description) {
        return messageRepository.save(new Message(title, description));
    }

}