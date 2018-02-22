package com.hashtagplus.controller;

import com.hashtagplus.model.*;
import com.hashtagplus.model.form.MessageFormData;
import com.hashtagplus.service.HashtagService;
import com.hashtagplus.service.MessageHashtagService;
import com.hashtagplus.service.MessageService;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class MessageController {

  @Autowired
  private MessageService messageService;

  @Autowired
  private HashtagService hashtagService;

  @Autowired
  private MessageHashtagService messageHashtagService;


  @GetMapping("/save")
  @ResponseBody
  public String save() {
    List<Hashtag> hashtags = new ArrayList<>();

    Hashtag h1 = new Hashtag("tree");
    Hashtag h2 = new Hashtag("egg");

    h1.id = new ObjectId().toString();
    h2.id = new ObjectId().toString();

    hashtags.add(h1);
    hashtags.add(h2);

    Message m = new Message("test2", "new message2", "0");
    m.setHashtags(hashtags);
    //  m.id = new ObjectId();

    // h1.setMessage(m);
    // h2.setMessage(m);

    hashtagService.saveHashtag(h1);
    hashtagService.saveHashtag(h2);

    m = this.messageService.saveMessage(m);

    return m.toString();
  }

  @Secured({"ROLE_USER"})
  @RequestMapping(method = GET, value = {"/messages/m/{id}"})
  public ModelAndView message(
          @PathVariable("id") String id) {

    Message message = ObjectId.isValid(id) ?
            messageService.getMessageById(id) :
            messageService.getMessageBySlug(id);

    ModelAndView mav = new ModelAndView("message");
    mav.addObject("message", message);
    return mav;
  }

  @RequestMapping(method = GET, value = {"/messages"})
  public ModelAndView getMessages(
          @AuthenticationPrincipal UserDetails user,
          @RequestParam(value = "sortby", defaultValue = "created_at") String sortby,
          @RequestParam(value = "order", defaultValue = "asc") String order,
          @RequestParam(value = "page", defaultValue = "1") int page,
          @RequestParam(value = "limit", defaultValue = "3") int limit,
          @RequestParam(value = "hashtag", defaultValue = "") String hashtags) {

    HtplUser htplUser = (HtplUser) user;
    Result result = getMessages(htplUser, sortby, order, page, limit, hashtags);

    List<Message> messages = result.messages;
    List<Message> withMedia = messages.stream()
            .filter(m -> m.hasImage)
            .collect(Collectors.toList());

    ModelAndView mav = new ModelAndView("messages");
    mav.addObject("messageFormData", new MessageFormData());
    mav.addObject("messages", messages);
    mav.addObject("limit", (limit >= result.totalNumberOfMessages) ? result.totalNumberOfMessages : limit);
    mav.addObject("total_messages", result.totalNumberOfMessages);
    mav.addObject("total_pages", result.totalNumberOfPages);

    mav.addObject("from", page);
    mav.addObject("to", limit);

    mav.addObject("withmedia", withMedia);
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
    List<Message> messages = this.messageHashtagService.getMessagesWithTopicAndHashtags(topic, hashtags);

    List<Message> withMedia = messages.stream()
            .filter(m -> m.hasImage)
            .collect(Collectors.toList());

    ModelAndView mav = new ModelAndView("messages");
    mav.addObject("messageFormData", new MessageFormData());
    mav.addObject("messages", messages);

    mav.addObject("withmedia", withMedia);
    mav.addObject("user", null);
    return mav;
  }


  private Result getMessages(HtplUser htplUser, String sortby, String order, int page, int limit, String hashtags) {
    Sort sort = new Sort(
            order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortby);
    Result result = new Result();
    Page<Message> messages;
    if (!hashtags.equals("")) {
      Page<MessageHashtag> messageHashtags = this.messageHashtagService.getMessagesWithHashtag(hashtags, htplUser, sort, page, limit);
      List<Message> mes = messageHashtags.getContent().stream().map(MessageHashtag::getMessage).distinct().collect(Collectors.toList());
      result.messages = mes;
      result.totalNumberOfMessages = messageHashtags.getTotalElements();
      result.totalNumberOfPages = messageHashtags.getTotalPages();
    } else {
      messages = this.messageService.getAllMessages(htplUser, sort, page, limit);
      result.messages = messages.getContent();
      result.totalNumberOfMessages = messages.getTotalElements();
      result.totalNumberOfPages = messages.getTotalPages();
    }
    return result;
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


  static class Result {
    List<Message> messages;
    long totalNumberOfMessages;
    int totalNumberOfPages;

    Result() {}

  }


}