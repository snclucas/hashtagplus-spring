package com.hashtagplus.service;

import com.hashtagplus.model.Hashtag;
import com.hashtagplus.model.Message;
import com.hashtagplus.model.MessageHashtag;
import com.hashtagplus.model.repo.MessageHashtagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageHashtagService {

    @Autowired
    private MessageHashtagRepository messageHashtagRepository;

    public MessageHashtag saveMessageHashtag(Message message, Hashtag hashtag) {
        MessageHashtag mh = new MessageHashtag(message, hashtag.getText());
        return messageHashtagRepository.save(mh);
    }

    public List<MessageHashtag> getMessagesWithHashtag(Hashtag hashtag) {
        return messageHashtagRepository.findByHashtag(hashtag.getText());
    }

    public List<Message> getMessagesWithHashtags(List<Hashtag> hashtags) {
        return messageHashtagRepository.findAllWithHashtags(hashtags);
    }

    public List<MessageHashtag> getHashtagsWithMessage(Message message) {
        return messageHashtagRepository.findByMessage(message);
    }

    public void deleteAllForMessage(Message message) {
        messageHashtagRepository.deleteMessageHashtagsByMessage(message);
    }


}