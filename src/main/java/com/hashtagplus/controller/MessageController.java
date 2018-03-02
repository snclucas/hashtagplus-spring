package com.hashtagplus.controller;

import com.hashtagplus.model.*;
import com.hashtagplus.model.form.*;
import com.hashtagplus.service.HashtagService;
import com.hashtagplus.service.MessageHashtagService;
import com.hashtagplus.service.MessageService;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class MessageController {

  @Autowired
  private MessageService messageService;

  @Autowired
  private MessageHashtagService messageHashtagService;


  @Secured({"ROLE_USER"})
  @RequestMapping(method = GET, value = {"/messages/m/{id}"})
  public ModelAndView message(
          @AuthenticationPrincipal UserDetails user,
          @PathVariable("id") String id,
          @RequestParam(value = "sortby", defaultValue = "created_at") String sortby,
          @RequestParam(value = "order", defaultValue = "asc") String order,
          @RequestParam(value = "page", defaultValue = "1") int page,
          @RequestParam(value = "limit", defaultValue = "100") int limit,
          @RequestParam(value = "privacy", defaultValue = "public") String privacy,
          @RequestParam(value = "hashtag", defaultValue = "") String hashtags) {

    Sort sort = new Sort(
            order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortby);
    HtplUser htplUser = (HtplUser) user;

    Message message = ObjectId.isValid(id) ?
            messageService.getMessageById(id) :
            messageService.getMessageBySlug(id);

    List<MessageHashtag> messageHashtags = messageHashtagService.getHashtagsWithMessage(message);

    Page<MessageHashtag> commentsMH = messageService.getCommentsForMessageById(message.id, htplUser, sort, page, limit, privacy);

    List<Message> comments = commentsMH.getContent().stream()
            .map(MessageHashtag::getMessage)
            .collect(Collectors.toList());

    ModelAndView mav = new ModelAndView("message");
    mav.addObject("messageFormData", new MessageFormData());
    mav.addObject("message", message);
    mav.addObject("messageHashtags", messageHashtags);
    mav.addObject("topic", "");
    mav.addObject("comments", comments);
    mav.addObject("user", htplUser);
    return mav;
  }

  @RequestMapping(method = GET, value = {"/messages"})
  public ModelAndView getMessages(
          @AuthenticationPrincipal UserDetails user,
          @RequestParam(value = "sortby", defaultValue = "created_at") String sortby,
          @RequestParam(value = "order", defaultValue = "asc") String order,
          @RequestParam(value = "page", defaultValue = "1") int page,
          @RequestParam(value = "limit", defaultValue = "100") int limit,
          @RequestParam(value = "privacy", defaultValue = "public") String privacy,
          @RequestParam(value = "hashtag", defaultValue = "") String hashtags) {

    Sort sort = new Sort(
            order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortby);
    HtplUser htplUser = (HtplUser) user;

    Page<MessageHashtag> result = getMessages(htplUser, sortby, order, page, limit, hashtags, privacy);


    List<Message> messages = result.getContent().stream()
            .map(MessageHashtag::getMessage)
            .filter(m -> m.getParent() == null || m.getParent().equals(""))
            .collect(Collectors.toList());

    List<Message> withMedia = messages.stream()
            .filter(Message::hasMedia)
            .collect(Collectors.toList());

    Page<MessageHashtag> eventsMH = this.messageHashtagService.getMessagesWithHashtag("event", htplUser, sort, page, limit, privacy);

    List<Message> events = eventsMH.getContent().stream()
            .map(MessageHashtag::getMessage)
            .collect(Collectors.toList());

    ModelAndView mav = new ModelAndView("messages");
    mav.addObject("messageFormData", new MessageFormData());
    mav.addObject("messages", messages);
    mav.addObject("withMedia", withMedia);
    mav.addObject("events", events);
    mav.addObject("limit", (limit >= result.getTotalElements()) ? result.getTotalElements() : limit);
    mav.addObject("total_messages", result.getTotalElements());
    mav.addObject("total_pages", result.getTotalPages());
    mav.addObject("current_user", htplUser.getUsername());
    mav.addObject("from", page);
    mav.addObject("to", limit);

    mav.addObject("withmedia", withMedia);
    mav.addObject("topic", "");
    mav.addObject("parent", "none");
    mav.addObject("user", null);
    return mav;
  }

  @RequestMapping(method = GET, value = {"/messages/{topic}"})
  public ModelAndView getMessages(
          @AuthenticationPrincipal UserDetails user,
          @PathVariable("topic") String topic,
          @RequestParam(value = "sortby", defaultValue = "created_at") String sortby,
          @RequestParam(value = "order", defaultValue = "asc") String order,
          @RequestParam(value = "page", defaultValue = "1") int page,
          @RequestParam(value = "limit", defaultValue = "100") int limit,
          @RequestParam(value = "hashtag", defaultValue = "") String hashtags) {

    HtplUser htplUser = (HtplUser) user;
    Sort sort = new Sort(
            order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortby);

    Page<MessageHashtag> messageHashtags = this.messageHashtagService.getMessagesWithTopicAndHashtags(topic, hashtags, htplUser, sort, page, limit);

    List<Message> messages = messageHashtags.getContent()
            .stream().map(MessageHashtag::getMessage)
            .distinct()
            .collect(Collectors.toList());

    List<Message> withMedia = messages.stream()
            .filter(Message::hasMedia)
            .collect(Collectors.toList());

    List<Message> events = messageHashtags.getContent().stream()
            .filter(mh -> mh.getHashtag().getText().equalsIgnoreCase("event"))
            .map(MessageHashtag::getMessage)
            .collect(Collectors.toList());

    ModelAndView mav = new ModelAndView("messages");
    mav.addObject("messageFormData", new MessageFormData());
    mav.addObject("events", events);
    mav.addObject("messages", messages);
    mav.addObject("limit", (limit >= messageHashtags.getTotalElements()) ? messageHashtags.getTotalElements() : limit);
    mav.addObject("total_messages", messageHashtags.getTotalElements());
    mav.addObject("total_pages", messageHashtags.getTotalPages());
    mav.addObject("withmedia", withMedia);
    mav.addObject("topic", topic);
    mav.addObject("parent", "none");
    mav.addObject("user", null);
    return mav;
  }

  private Page<MessageHashtag> getMessages(HtplUser htplUser, String sortby, String order,
                                           int page, int limit, String hashtags, String privacy) {
    Sort sort = new Sort(
            order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortby);
    if (!hashtags.equals("")) {
      return this.messageHashtagService.getMessagesWithHashtag(hashtags, htplUser, sort, page, limit, privacy);
    } else {
      return this.messageService.getAllMessages(htplUser, sort, page, limit, privacy);
    }
  }

  @Secured({"ROLE_USER"})
  @RequestMapping(method = POST, value = {"/messages/add"})
  public String addMessages(@AuthenticationPrincipal UserDetails user,
                            @ModelAttribute(value = "messageFormData") MessageFormData messageFormData) {
    HtplUser htplUser = (HtplUser) user;
    messageHashtagService.saveMessageWithHashtags(messageFormData, htplUser);
    //THIS DOESNT WORK, ALSO LOOK AT USERS
    return "messages";
  }


  @RequestMapping(method = GET, value = {"/test"})
  public ModelAndView getTest() {

    ModelAndView mav = new ModelAndView("test");

    return mav;
  }


}