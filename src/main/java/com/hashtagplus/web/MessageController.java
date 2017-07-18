package com.hashtagplus.web;

import com.hashtagplus.model.MessageRepository;
import com.hashtagplus.model.User;
import com.hashtagplus.model.UserRepository;
import com.hashtagplus.service.MessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/hello")
    @ResponseBody
    public String helloWorld() {
        return this.messageService.getAllMessages().toString();
    }

    @GetMapping("/save")
    @ResponseBody
    public String save() {
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();

        list.add(new SimpleGrantedAuthority("ROLE_" + "TEST"));

        this.userRepository.save(new User("simon", "password", list));
        return this.messageService.saveMessage("test", "new message").toString();
    }
}