package com.hashtagplus.service;

import com.hashtagplus.model.Hashtag;
import com.hashtagplus.model.HtplUser;
import com.hashtagplus.model.HtplUserDetails;
import com.hashtagplus.model.Message;
import com.hashtagplus.model.repo.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
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

    public List<Message> getAllMessages(HtplUser user, Sort sort, int pageNumber, int limit) {
        Pageable request =
                new PageRequest(pageNumber - 1, limit, sort);
        return messageRepository.findAll(user.getId(), request);
    }

    public Message getMessageById(String id) {
        return messageRepository.findOne(id);
    }

    public List<Message> getMessagesByUserAndHashtag(HtplUser user, String hashtag) {
        return messageRepository.findByUserIdAndHashtags(user.getId(), hashtag);
    }

    public List<Message> getMessageByHashtag(String hashtag) {
        return messageRepository.findByHashtag(hashtag);
    }

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    public void deleteMessage(Message message) {
        messageRepository.delete(message);
    }

    public void deleteMessage(String id) {
        messageRepository.delete(id);
    }

}