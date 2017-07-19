package com.hashtagplus.rest;

import com.hashtagplus.model.Message;
import com.hashtagplus.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Secured({"ROLE_ADMIN"})
    @RequestMapping(method=GET, value={"/message/{id}"})
    public Message message(
            @PathVariable("id") String id,
            @RequestParam(value="test", defaultValue="") String test) {
        if(test.equals("egg"))
            return messageService.getMessageById(id);
        else
            return new Message("Oops", "Egg", new ArrayList<String>());
    }

}
