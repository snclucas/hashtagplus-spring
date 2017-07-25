package com.hashtagplus.controller;

import com.hashtagplus.model.Hashtag;
import com.hashtagplus.model.Message;
import com.hashtagplus.model.form.MessageFormData;
import com.hashtagplus.model.repo.UserDetailsRepository;
import com.hashtagplus.service.HashtagService;
import com.hashtagplus.service.MessageHashtagService;
import com.hashtagplus.service.MessageService;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private HashtagService hashtagService;

    @Autowired
    private UserDetailsRepository userRepository;

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

        Message m = new Message("test2", "new message2");
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
    @RequestMapping(method=GET, value={"/message/{id}"})
    public ModelAndView message(
            @PathVariable("id") String id) {
        Message message =  messageService.getMessageById(id);
        ModelAndView mav = new ModelAndView("message");
        mav.addObject("message", message);
        return mav;
    }

    @RequestMapping(method=GET, value={"/messages"})
    public ModelAndView getMessages(
            @RequestParam(value="sortby", defaultValue="created_at") String sortby,
            @RequestParam(value="order", defaultValue="asc") String order,
            @RequestParam(value="page", defaultValue="1") int page,
            @RequestParam(value="limit", defaultValue="100") int limit) {

        Sort sort = new Sort(
                order.equalsIgnoreCase("asc")?Sort.Direction.ASC:Sort.Direction.DESC, sortby);

        List<Message> messages =  this.messageService.getAllMessages(sort, page, limit);
        ModelAndView mav = new ModelAndView("messages");
        mav.addObject("messageFormData", new MessageFormData());
        mav.addObject("messages", messages);
        return mav;
    }




    @Secured({"ROLE_USER"})
    @RequestMapping(method=POST, value={"/messages/add"})
    public Message addMessages(@ModelAttribute(value = "messageFormData") MessageFormData messageFormData ) {
        return messageHashtagService.saveMessageWithHashtags(messageFormData);
    }





}