package com.hashtagplus.rest;

import com.hashtagplus.model.Message;
import com.hashtagplus.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

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
}
