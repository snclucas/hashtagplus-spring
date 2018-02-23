package com.hashtagplus.service;

import com.hashtagplus.model.*;
import com.hashtagplus.model.form.MessageFormData;
import com.hashtagplus.model.repo.AggDao;
import com.hashtagplus.model.repo.MessageHashtagRepository;
import com.hashtagplus.model.util.HTMLUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import java.util.*;
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

    String messageText = messageFormData.getText();

    HTMLUtils.TextComponents textComponents = HTMLUtils.processText(messageText);

    List<MediaUrl> urls = HTMLUtils.extractMediaURLS(textComponents.urls);
    if(urls.size() > 0) {
      message.setContentType(urls.get(0).getContentType());
    }

    // Add all found media sources
    urls.stream()
            .filter(mu -> HTMLUtils.isMediaUrl(mu.getContentType()))
            .forEach(message::addMediaUrl);

    message.hasText = textComponents.hasText;

    List<Hashtag> hashtagList = new ArrayList<>();

    for (String hashtag : textComponents.hashtags) {
      if (!hashtag.equals(" ")) {
        hashtag = hashtag.trim();
        if(hashtag.startsWith("#")) {
          hashtag = hashtag.substring(1,hashtag.length()-1);
        }
        Hashtag hashtagToSave = new Hashtag(hashtag.replace(" ", " "));
        hashtagToSave = hashtagService.saveHashtag(hashtagToSave);
        hashtagList.add(hashtagToSave);
        saveMessageHashtag(message, hashtagToSave, user);
      }
    }
    message.setHashtags(hashtagList);
    return messageService.saveMessage(message);
  }

  public Page<MessageHashtag> getMessagesWithHashtag(String hashtagsText, HtplUser user, Sort sort, int pageNumber, int limit) {
    Pageable request =
            new PageRequest(pageNumber - 1, limit, sort);
   // hashtagsText = hashtagsText.replace(" ", "");
    List<String> hashTagsTextList = Arrays.asList(hashtagsText.split(","));

    List<Hashtag> hashtagList = hashTagsTextList.stream()
            .map(ht -> hashtagService.findHashtag(ht))
            .collect(Collectors.toList());

    return messageHashtagRepository.findMessageHashtagsByHashtagIdIn(hashtagList, request);
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


  public Page<MessageHashtag> getMessagesWithTopicAndHashtags(String topic, String hashtagsText, HtplUser user, Sort sort, int pageNumber, int limit) {
    Pageable request = new PageRequest(pageNumber - 1, limit, sort);

    hashtagsText = hashtagsText.replace(" ", "");

    Hashtag topicHashtag = hashtagService.findHashtag(topic);

    List<String> hashTagsTextList = hashtagsText.equals("") ? Collections.emptyList() : Arrays.asList(hashtagsText.split(","));

    List<Hashtag> hashtagList = hashTagsTextList.stream()
            .map(ht -> hashtagService.findHashtag(ht))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

    return messageHashtagRepository.getMessagesWithTopicAndHashtags(topicHashtag, hashtagList, request);
  }

}