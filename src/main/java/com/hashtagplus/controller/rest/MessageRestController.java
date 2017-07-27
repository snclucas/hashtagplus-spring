package com.hashtagplus.controller.rest;

import com.hashtagplus.model.*;
import com.hashtagplus.model.form.MessageFormData;
import com.hashtagplus.service.HashtagService;
import com.hashtagplus.service.MessageHashtagService;
import com.hashtagplus.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

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


    @RequestMapping(method=GET, value={"/api/message/{id}"})
    public Message message(
            @PathVariable("id") String id,
            @RequestParam(value="test", defaultValue="") String test) {
        if(test.equals("egg"))
            return messageService.getMessageById(id);
        else
            return new Message("Oops", "Egg", "0");
    }

    @RequestMapping(method=GET, value={"/api/messages"})
    public List<Message> getMessages(
            @RequestParam(value="sortby", defaultValue="created_at") String sortby,
            @RequestParam(value="order", defaultValue="asc") String order,
            @RequestParam(value="page", defaultValue="1") int page,
            @RequestParam(value="limit", defaultValue="100") int limit) {
        HtplUserDetails user = (HtplUserDetails) context.getAttribute("user_from_token");
        Sort sort = new Sort(
                order.equalsIgnoreCase("asc")?Sort.Direction.ASC:Sort.Direction.DESC, sortby);
        return this.messageService.getAllMessages(user, sort, page, limit);
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

    private Message createMessage(MessageFormData messageFormData ) {
        HtplUser user = (HtplUser) context.getAttribute("user_from_token");
        return messageHashtagService.saveMessageWithHashtags(messageFormData, user);
    }









    @RequestMapping(method=POST, value={"/api/messages/delete/{message_id}"})
    public Message deleteMessages(@PathVariable("message_id") String message_id) {
        Message message = messageService.getMessageById(message_id);
        messageService.deleteMessage(message);
        messageHashtagService.deleteAllForMessage(message);
        return message;
    }


    @RequestMapping(method=GET, value={"/api/messages/2"})
    public List<MessageHashtag> getMessagesWithHashtags(@RequestParam(value="hashtags", defaultValue="") String hashtags) {
        List<String> hashtagsList = Arrays.asList(hashtags.split(","));

        String[] hashtagsArr = hashtags.split(",");
        List<MessageHashtag> messages = messageHashtagService.getMessagesWithHashtag(hashtagsArr[0]);

        List<MessageHashtag> messages2 = messageHashtagService.getMessagesWithHashtags(hashtagsList);
        return messages;
    }



}
