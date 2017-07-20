package com.hashtagplus.service;

import com.hashtagplus.model.Message;
import com.hashtagplus.model.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public List<Message> getAllMessages(Sort sort, int pageNumber, int limit) {
        Pageable request =
                new PageRequest(pageNumber - 1, limit, sort);
        return messageRepository.findAll(request).getContent();
    }

    public Message getMessageById(String id) {
        return messageRepository.findOne(id);
    }

    public List<Message> getMessageByHashtag(String hashtag) {
        return messageRepository.findByHashtag(hashtag);
    }

    public Message saveMessage(String title, String description, List<String> hashtags) {
        return saveMessage(new Message(title, description, hashtags));
    }

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

}