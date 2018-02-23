package com.hashtagplus.service;

import com.hashtagplus.model.*;
import com.hashtagplus.model.repo.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.*;

@Component
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Value("${name:World}")
    private String name;

    public String getHelloMessage() {
        return "Hello " + this.name;
    }

    public Page<MessageHashtag> getAllMessages(HtplUser user, Sort sort, int pageNumber, int limit) {
        Pageable request =
                new PageRequest(pageNumber - 1, limit, sort);

      Page<Message> messages = messageRepository.findAll(user.getId(), request);

      List<MessageHashtag> messageHashtags =
              messages.getContent().stream()
                      .map(m -> new MessageHashtag(m, new Hashtag(), user.getId())).collect(Collectors.toList());

      Pageable pageable = new PageRequest(pageNumber - 1, limit, sort);
      int start = pageable.getOffset();
      int end = (start + pageable.getPageSize()) > messages.getContent().size() ? messages.getContent().size() : (start + pageable.getPageSize());
      return new PageImpl<>(messageHashtags.subList(start, end), pageable, messageHashtags.size());
    }

    public Message getMessageById(String id) {
        return messageRepository.findOne(id);
    }

    public Message getMessageBySlug(String slug) {
        return messageRepository.findOneBySlug(slug);
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