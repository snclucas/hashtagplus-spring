package com.hashtagplus.rest;

import com.hashtagplus.model.Hashtag;
import com.hashtagplus.model.Message;
import com.hashtagplus.model.form.MessageFormData;
import com.hashtagplus.service.HashtagService;
import com.hashtagplus.service.MessageHashtagService;
import com.hashtagplus.service.MessageService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class HtplRestController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private HashtagService hashtagService;

    @Autowired
    private MessageHashtagService messageHashtagService;

    @Secured({"ROLE_USER"})
    @RequestMapping(method=GET, value={"/api/message/{id}"})
    public Message message(
            @PathVariable("id") String id,
            @RequestParam(value="test", defaultValue="") String test) {
        if(test.equals("egg"))
            return messageService.getMessageById(id);
        else
            return new Message("Oops", "Egg");
    }

    @Secured({"ROLE_USER"})
    @RequestMapping(method=GET, value={"/api/messages"})
    public List<Message> getMessages(
            @RequestParam(value="sortby", defaultValue="created_at") String sortby,
            @RequestParam(value="order", defaultValue="asc") String order,
            @RequestParam(value="page", defaultValue="1") int page,
            @RequestParam(value="limit", defaultValue="100") int limit) {

        Sort sort = new Sort(
                order.equalsIgnoreCase("asc")?Sort.Direction.ASC:Sort.Direction.DESC, sortby);

        List<Message> messages =  this.messageService.getAllMessages(sort, page, limit);

        return messages;
    }

    @Secured({"ROLE_USER"})
    @RequestMapping(method=POST, value={"/api/messages/add"})
    public Message addMessages(@ModelAttribute(value = "messageFormData") MessageFormData messageFormData ) {
        Message message = new Message(messageFormData.getTitle(), messageFormData.getText());
        message.id = new ObjectId();

        List<Hashtag> hashtagList = new ArrayList<>();
        String[] hashtags = messageFormData.getHashtags().split(",");
        for(String hashtag : hashtags) {
            Hashtag hashtagToSave = new Hashtag(hashtag);
            hashtagToSave = hashtagService.saveHashtag(hashtagToSave);
            hashtagList.add(hashtagToSave);
            messageHashtagService.saveMessageHashtag(message, hashtagToSave);
        }
        message.setHashtags(hashtagList);
        return messageService.saveMessage(message);
    }



    @Secured({"ROLE_USER"})
    @RequestMapping(method=POST, value={"/api/messages/delete/{message_id}"})
    public Message deleteMessages(@PathVariable("message_id") String message_id) {
        Message message = messageService.getMessageById(message_id);
        messageService.deleteMessage(message);
        messageHashtagService.deleteAllForMessage(message);
        return message;
    }



}
