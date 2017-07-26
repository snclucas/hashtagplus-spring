package com.hashtagplus.controller;

import com.hashtagplus.model.HtplUserDetails;
import com.hashtagplus.model.Message;
import com.hashtagplus.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static java.awt.SystemColor.text;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class WebsocketController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private HttpServletRequest context;

    @RequestMapping(method=GET, value = "/greeting")
    String index(){
        return "websocket";
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public List<Message> greeting(@AuthenticationPrincipal HtplUserDetails user,
                                  String message) throws Exception {
        //Thread.sleep(1000); // simulated delay

        String order = "desc";
        String sortby = "_id";
        Sort sort = new Sort(
                order.equalsIgnoreCase("asc")?Sort.Direction.ASC:Sort.Direction.DESC, sortby);

        List<Message> messages = messageService.getAllMessages(user, sort, 1, 100);

        this.template.convertAndSend("/topic/greetings", messages);

        Thread.sleep(1000);

        return messages;

       // return new Message("Hello, " + message.getText() + "!", "asdasdasd", new ArrayList<>());
    }



    @MessageMapping("/hashtags")
    @SendTo("/topic/greetings2")
    public String getHashtags(String message) throws Exception {
        return "ddd";
    }

}