package com.hashtagplus.rest;

import com.hashtagplus.model.HtplUserDetails;
import com.hashtagplus.model.Message;
import com.hashtagplus.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class HtplRestController {

    @Autowired
    private MessageService messageService;

    @Secured({"ROLE_USER"})
    @RequestMapping(method=GET, value={"/api/message/{id}"})
    public Message message(
            @PathVariable("id") String id,
            @RequestParam(value="test", defaultValue="") String test) {
        if(test.equals("egg"))
            return messageService.getMessageById(id);
        else
            return new Message("Oops", "Egg", new ArrayList<>());
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
    public Message addMessages(@ModelAttribute(value = "message") Message message) {
        message.setSlug(message.getTitle());
        message.setCreatedOnNow();
        return messageService.saveMessage(message);
    }
}
