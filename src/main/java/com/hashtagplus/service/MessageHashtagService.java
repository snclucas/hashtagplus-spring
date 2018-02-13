package com.hashtagplus.service;

import com.hashtagplus.model.*;
import com.hashtagplus.model.form.MessageFormData;
import com.hashtagplus.model.repo.AggDao;
import com.hashtagplus.model.repo.MessageHashtagRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MessageHashtagService {

    @Autowired
    private MessageHashtagRepository messageHashtagRepository;

    @Autowired
    private MessageService messageService;

    @Autowired
    private HashtagService hashtagService;

    public MessageHashtag saveMessageHashtag(Message message, Hashtag hashtag, HtplUser user) {
        MessageHashtag mh = new MessageHashtag(message, hashtag, user.getId());
        return messageHashtagRepository.save(mh);
    }


    public Message saveMessageWithHashtags(MessageFormData messageFormData, HtplUser user) {
        Message message = new Message(messageFormData.getTitle(), messageFormData.getText(), user.getId());

        List<Hashtag> hashtagList = new ArrayList<>();
        String[] hashtags = new String[]{};
        if(messageFormData.getHashtags()!=null)
            hashtags = messageFormData.getHashtags().split(",");

        for(String hashtag : hashtags) {
            Hashtag hashtagToSave = new Hashtag(hashtag.replace(" ", ""));
            hashtagToSave = hashtagService.saveHashtag(hashtagToSave);
            hashtagList.add(hashtagToSave);
            saveMessageHashtag(message, hashtagToSave, user);
        }
        message.setHashtags(hashtagList);
        return messageService.saveMessage(message);
    }

    public List<Message> getMessagesWithHashtag(String hashtagsText) {

      hashtagsText = hashtagsText.replace(" ", "");
      List<String> hashTags = Arrays.asList(hashtagsText.split("'"));

      List<String> fss = hashTags.stream()
              .map(ht -> hashtagService.findHashtag(ht))
              .map(htt -> htt.)
              .collect(Collectors.toList());



      Hashtag hashtag = new Hashtag(hashtagsText);

      Hashtag ht = hashtagService.findHashtag(hashtagsText);
      Hashtag ht2 = hashtagService.findHashtag("poo");
   //   messageHashtagRepository.

      List<String> hashList= new ArrayList<>();
      hashList.add("cat");

      List<ObjectId> hashIDList= new ArrayList<>();
      hashIDList.add(new ObjectId(ht.id));

      List<Hashtag> hashStrList= new ArrayList<>();
      hashStrList.add(ht);
      hashStrList.add(ht2);

      List<MessageHashtag> d = messageHashtagRepository.findByHashtagIn(hashList);

        List<MessageHashtag> d2 =messageHashtagRepository.findByHashtag(new ObjectId(ht.id));

        List<MessageHashtag> d3 = messageHashtagRepository.findMessageHashtagsByHashtagIdIn(hashStrList);



        return d.stream().map(mh -> mh.getMessage()).collect(Collectors.toList());
    }

    public List<MessageHashtag> getMessagesWithHashtags(List<String> hashtags) {
        return messageHashtagRepository.findByHashtagIn(hashtags);
    }

    public List<MessageHashtag> getHashtagsWithMessage(Message message) {
        return messageHashtagRepository.findByMessage(message);
    }

    public void deleteAllForMessage(Message message) {
        messageHashtagRepository.deleteMessageHashtagsByMessage(message);
    }

    public List<AggDao> aggregate(HtplUser user) {
        return messageHashtagRepository.aggregate(user);
    }


}