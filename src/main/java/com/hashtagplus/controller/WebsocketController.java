package com.hashtagplus.controller;

import com.hashtagplus.model.HtplUser;
import com.hashtagplus.model.HtplUserDetails;
import com.hashtagplus.model.Message;
import com.hashtagplus.model.repo.AggDao;
import com.hashtagplus.service.HashtagService;
import com.hashtagplus.service.MessageHashtagService;
import com.hashtagplus.service.MessageService;
import com.hashtagplus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageHashtagService messageHastageService;

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private HttpServletRequest context;

    @RequestMapping(method = GET, value = "/greeting")
    String index() {
        return "websocket";
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public List<Message> greeting(@AuthenticationPrincipal Principal user,
                                  String message) throws Exception {
        //Thread.sleep(1000); // simulated delay

        String order = "desc";
        String sortby = "_id";
        Sort sort = new Sort(
                order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortby);

        HtplUser htplUser = new HtplUser(user.getName(), "", "", new ArrayList<GrantedAuthority>());
        List<Message> messages = messageService.getAllMessages(htplUser, sort, 1, 100);



        this.template.convertAndSend("/topic/greetings", new Object[]{messages, user});

        Thread.sleep(1000);

        return messages;

        // return new Message("Hello, " + message.getText() + "!", "asdasdasd", new ArrayList<>());
    }


    @MessageMapping("/hashtags")
    @SendTo("/topic/greetings2")
    public List<AggDao> getHashtags(@Payload String message, Principal principal) throws Exception {

        HtplUserDetails htplUserDetails = userService.getUserByUsername(principal.getName());
        HtplUser htplUser = new HtplUser(htplUserDetails.getUsername(),
                htplUserDetails.getPassword(),
                htplUserDetails.id, htplUserDetails.getAuthorities());

        return messageHastageService.aggregate(htplUser);
    }

}