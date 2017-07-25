package com.hashtagplus.service;

import com.hashtagplus.model.Hashtag;
import com.hashtagplus.model.Message;
import com.hashtagplus.model.MessageHashtag;
import com.hashtagplus.model.form.MessageFormData;
import com.hashtagplus.model.repo.MessageHashtagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MessageHashtagService {

    @Autowired
    private MessageHashtagRepository messageHashtagRepository;

    @Autowired
    private MessageService messageService;

    @Autowired
    private HashtagService hashtagService;

    public MessageHashtag saveMessageHashtag(Message message, Hashtag hashtag) {
        MessageHashtag mh = new MessageHashtag(message, hashtag.getText());
        return messageHashtagRepository.save(mh);
    }


    public Message saveMessageWithHashtags(MessageFormData messageFormData) {
        Message message = new Message(messageFormData.getTitle(), messageFormData.getText());

        List<Hashtag> hashtagList = new ArrayList<>();
        String[] hashtags = new String[]{};
        if(messageFormData.getHashtags()!=null)
            hashtags = messageFormData.getHashtags().split(",");

        for(String hashtag : hashtags) {
            Hashtag hashtagToSave = new Hashtag(hashtag.replace(" ", ""));
            hashtagToSave = hashtagService.saveHashtag(hashtagToSave);
            hashtagList.add(hashtagToSave);
            saveMessageHashtag(message, hashtagToSave);
        }
        message.setHashtags(hashtagList);
        return messageService.saveMessage(message);
    }

    public List<MessageHashtag> getMessagesWithHashtag(String hashtag) {
        List<MessageHashtag> d = messageHashtagRepository.findByHashtag(hashtag);
        return d;
    }

    public List<MessageHashtag> getMessagesWithHashtags(List<String> hashtags) {
        return messageHashtagRepository.findByHashtagsIn(hashtags);
    }

    public List<MessageHashtag> getHashtagsWithMessage(Message message) {
        return messageHashtagRepository.findByMessage(message);
    }

    public void deleteAllForMessage(Message message) {
        messageHashtagRepository.deleteMessageHashtagsByMessage(message);
    }


}