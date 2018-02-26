package com.hashtagplus.service;

import com.hashtagplus.model.*;
import com.hashtagplus.model.form.*;
import com.hashtagplus.model.repo.MessageRepository;
import org.bson.types.ObjectId;
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

  @Autowired
  private MessageHashtagService messageHashtagService;

  @Autowired
  private HashtagService hashtagService;

  @Value("${name:World}")
  private String name;


  public Page<MessageHashtag> getCommentsForMessageById(String parent, HtplUser user, Sort sort, int pageNumber, int limit, String privacy) {
    return getAllMessages(user, sort, pageNumber, limit, privacy, parent);
  }

  public Page<MessageHashtag> getAllMessages(HtplUser user, Sort sort, int pageNumber, int limit, String privacy) {
    return getAllMessages(user, sort, pageNumber, limit, privacy, "");
  }

  private Page<MessageHashtag> getAllMessages(HtplUser user, Sort sort, int pageNumber, int limit, String privacy, String parent) {
    Pageable request =
            new PageRequest(pageNumber - 1, limit, sort);

    Page<Message> messages = messageRepository.findMessagesByUsernameOrPrivacy(user.getUsername(), privacy, request);

    List<MessageHashtag> messageHashtags;
    if(ObjectId.isValid(parent)) {
      messageHashtags =
              messages.getContent().stream()
                      .filter(m -> m.getParent().equals(parent))
                      .map(m -> new MessageHashtag(m, new Hashtag(), user.getUsername()))
                      .collect(Collectors.toList());
    } else {
      messageHashtags =
              messages.getContent().stream()
                      .filter(m -> m.getParent().equals(""))
                      .map(m -> new MessageHashtag(m, new Hashtag(), user.getUsername()))
                      .collect(Collectors.toList());
    }

    Pageable pageable = new PageRequest(pageNumber - 1, limit, sort);
    int start = pageable.getOffset();
    int end = (start + pageable.getPageSize()) > messageHashtags.size() ? messageHashtags.size() : (start + pageable.getPageSize());
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
//
//  public Message saveComment(CommentFormData commentFormData, HtplUser user) {
//    Message parent = getMessageById(commentFormData.getParent());
//    if(parent != null) {
//      Message comment = new Message("", commentFormData.getText(), user.getUsername());
//      parent.addComment(comment);
//      return saveMessage(parent);
//    }
//    return null;
//  }

  public void deleteMessage(Message message) {
    messageRepository.delete(message);
  }

  public long deleteMessage(HtplUser user, String id) {
    Message messageToDelete = getMessageById(id);
    long result = 0;
    if(messageToDelete != null) {
      result = messageRepository.deleteMessageById(messageToDelete.id);
      if(result > 0) {
        messageHashtagService.deleteAllForMessage(messageToDelete);
      }
    }
    return result;
  }

}