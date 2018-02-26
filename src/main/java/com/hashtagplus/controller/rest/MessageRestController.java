package com.hashtagplus.controller.rest;

import com.hashtagplus.model.*;
import com.hashtagplus.model.form.*;
import com.hashtagplus.service.HashtagService;
import com.hashtagplus.service.MessageHashtagService;
import com.hashtagplus.service.MessageService;
import org.bson.types.ObjectId;
import org.json.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class MessageRestController {

  @Autowired
  private MessageService messageService;

  @Autowired
  private HashtagService hashtagService;

  @Autowired
  private MessageHashtagService messageHashtagService;

  @Autowired
  private HttpServletRequest context;


  @RequestMapping(method=GET, value={"/api/messages/m/{id}"})
  public Message message(
          @PathVariable("id") String id) {
    return ObjectId.isValid(id) ?
            messageService.getMessageById(id) :
            messageService.getMessageBySlug(id);
  }

  @RequestMapping(method=GET, value={"/api/messages"})
  public List<Message> getMessages(
          @RequestParam(value="sortby", defaultValue="created_at") String sortby,
          @RequestParam(value="order", defaultValue="asc") String order,
          @RequestParam(value="page", defaultValue="1") int page,
          @RequestParam(value="limit", defaultValue="100") int limit) {
    HtplUser user = (HtplUser) context.getAttribute("user_from_token");
    Sort sort = new Sort(
            order.equalsIgnoreCase("asc")?Sort.Direction.ASC:Sort.Direction.DESC, sortby);

    List<MessageHashtag> messages = this.messageService.getAllMessages(user, sort, page, limit, "public").getContent();
    return messages.stream()
            .map(MessageHashtag::getMessage)
            .collect(Collectors.toList());
  }







  @RequestMapping(method=POST, value={"/api/messages/{id}/delete"},
          produces={"application/json"},
          consumes={"application/json"})
  public String deleteMessageJSON(@PathVariable("id") String id) {
    return deleteMessage(id);
  }


  @RequestMapping(method=POST, value={"/api/messages/{id}/addcomment"},
          produces={"application/json"},
          consumes={"application/json"})
  public Message addCommentFromJSON(@RequestBody CommentFormData commentFormData ) {
    return addComment(commentFormData);
  }

  @RequestMapping(method = RequestMethod.POST, value={"/api/messages/{id}/addcomment"},
          headers = "content-type=application/x-www-form-urlencoded")
  public Message addCommentFromForm(@ModelAttribute CommentFormData commentFormData ) {
    return addComment(commentFormData);
  }





  @RequestMapping(method=POST, value={"/api/messages/add"},
          produces={"application/json"},
          consumes={"application/json"})
  public Message createMessagesFromJSON(@RequestBody MessageFormData messageFormData ) {
    return createMessage(messageFormData);
  }

  @RequestMapping(method = RequestMethod.POST, value={"/api/messages/add"},
          headers = "content-type=application/x-www-form-urlencoded")
  public Message createMessagesFromForm(@ModelAttribute MessageFormData messageFormData ) {
    return createMessage(messageFormData);
  }

  private Message addComment(CommentFormData commentFormData ) {
    HtplUser user = (HtplUser) context.getAttribute("user_from_token");
    return messageService.saveComment(commentFormData, user);
  }

  private Message createMessage(MessageFormData messageFormData ) {
    HtplUser user = (HtplUser) context.getAttribute("user_from_token");
    return messageHashtagService.saveMessageWithHashtags(messageFormData, user);
  }

  private String deleteMessage(String id) {
    HtplUser user = (HtplUser) context.getAttribute("user_from_token");
    long result = messageService.deleteMessage(user, id);

    JSONObject jsonString = new JSONObject();

    try {
      if(result > 0) {
        jsonString.put("result", "success");
        jsonString.put("message", "Entity with id " + id + " deleted.");
      } else {
        jsonString.put("result", "error");
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return jsonString.toString();
  }







  @RequestMapping(method=POST, value={"/api/messages/delete/{message_id}"})
  public Message deleteMessages(@PathVariable("message_id") String message_id) {
    Message message = messageService.getMessageById(message_id);
    messageService.deleteMessage(message);
    messageHashtagService.deleteAllForMessage(message);
    return message;
  }


  @RequestMapping(method=GET, value={"/api/messages/2"})
  public List<Message> getMessagesWithHashtags(
          @RequestParam(value="hashtags", defaultValue="") String hashtags,
          @RequestParam(value="privacy", defaultValue="public") String privacy) {
    String order = "asc";
    String sortby = "created_at";
    int limit = 100;
    int pageNumber = 1;
    Sort sort = new Sort(
            order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortby);

    List<String> hashtagsList = Arrays.asList(hashtags.split(","));

    String[] hashtagsArr = hashtags.split(",");
    HtplUser user = (HtplUser) context.getAttribute("user_from_token");

    Page<MessageHashtag> messages = messageHashtagService.getMessagesWithHashtag(hashtagsArr[0], user, sort, pageNumber, limit, privacy);

    List<MessageHashtag> messages2 = messageHashtagService.getMessagesWithHashtags(hashtagsList);

    List<Message> mes = messages.getContent().stream().map(MessageHashtag::getMessage).distinct().collect(Collectors.toList());

    return mes;
  }



}
