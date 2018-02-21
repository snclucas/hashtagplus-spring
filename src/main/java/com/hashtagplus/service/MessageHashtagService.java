package com.hashtagplus.service;

import com.hashtagplus.model.*;
import com.hashtagplus.model.form.MessageFormData;
import com.hashtagplus.model.repo.AggDao;
import com.hashtagplus.model.repo.MessageHashtagRepository;
import com.hashtagplus.model.util.HTMLUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
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

    String messageText = messageFormData.getText();

    List<String> urls = HTMLUtils.extractURLS(messageText);

    // Add all found image sources
    urls.stream()
            .filter(HTMLUtils::testImage)
            .forEach(message::addImageSource);
    message.setHasImage((message.imageSrc.size() > 0));

    List<Hashtag> hashtagList = new ArrayList<>();
    String[] hashtags = new String[]{};
    if (messageFormData.getHashtags() != null)
      hashtags = messageFormData.getHashtags().split(",");

    for (String hashtag : hashtags) {
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
    List<String> hashTagsTextList = Arrays.asList(hashtagsText.split(","));

    List<Hashtag> hashtagList = hashTagsTextList.stream()
            .map(ht -> hashtagService.findHashtag(ht))
            .collect(Collectors.toList());

    List<MessageHashtag> messageHashtags = messageHashtagRepository.findMessageHashtagsByHashtagIdIn(hashtagList);

    return messageHashtags.stream().map(MessageHashtag::getMessage).distinct().collect(Collectors.toList());
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


  public List<Message> getMessagesWithTopicAndHashtags(String topic, String hashtagsText) {
    hashtagsText = hashtagsText.replace(" ", "");

    Hashtag topicHashtag = hashtagService.findHashtag(topic);

    List<String> hashTagsTextList = hashtagsText.equals("") ? Collections.emptyList() : Arrays.asList(hashtagsText.split(","));

    List<Hashtag> hashtagList = hashTagsTextList.stream()
            .map(ht -> hashtagService.findHashtag(ht))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

    List<MessageHashtag> messageHashtags = messageHashtagRepository.getMessagesWithTopicAndHashtags(topicHashtag, hashtagList);
    return messageHashtags.stream().map(MessageHashtag::getMessage).collect(Collectors.toList());
  }

}