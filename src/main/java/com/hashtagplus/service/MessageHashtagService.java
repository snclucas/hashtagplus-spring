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
        MessageHashtag mh = new MessageHashtag(message, hashtag);
        return messageHashtagRepository.save(mh);
    }

    public List<Message> getMessagesWithHashtag(Hashtag hashtag) {
        return messageHashtagRepository.findByMessage_id(hashtag.id);
    }

    public List<Hashtag> getHashtagsWithMessage(Message message) {
        return messageHashtagRepository.findByHashtag_id(message.id);
    }

    public void deleteAllForMessage(Message message) {
        messageHashtagRepository.deleteMessageHashtagsByMessage(message);
    }


}