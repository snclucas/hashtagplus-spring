package com.hashtagplus.web;

import com.hashtagplus.model.HtplUserDetails;
import com.hashtagplus.model.Message;
import com.hashtagplus.model.UserDetailsRepository;
import com.hashtagplus.service.MessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserDetailsRepository userRepository;


    @GetMapping("/save")
    @ResponseBody
    public String save() {
       // List<GrantedAuthority> list = new ArrayList<>();

        //list.add(new SimpleGrantedAuthority("ROLE_" + "TEST"));

        //this.userRepository.save(new HtplUserDetails("simon", "password"));


        List<String> hashtags = new ArrayList<>();
        hashtags.add("tree");
        hashtags.add("egg");

        return this.messageService.saveMessage("test2", "new message2", hashtags).toString();
    }


    @RequestMapping(method=GET, value={"/messages"})
    public ModelAndView  getMessages(
            @RequestParam(value="sortby", defaultValue="created_at") String sortby,
            @RequestParam(value="order", defaultValue="asc") String order,
            @RequestParam(value="page", defaultValue="1") int page,
            @RequestParam(value="limit", defaultValue="100") int limit) {

        Sort sort = new Sort(
                order.equalsIgnoreCase("asc")?Sort.Direction.ASC:Sort.Direction.DESC, sortby);

        List<Message> messages =  this.messageService.getAllMessages(sort, page, limit);

        ModelAndView mav = new ModelAndView("messages");
        mav.addObject("messages", messages);
        return mav;
    }
}