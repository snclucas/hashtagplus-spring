package com.hashtagplus.controller;

import com.hashtagplus.model.HtplUserDetails;
import com.hashtagplus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserController {

    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/signup", method=RequestMethod.GET)
    public String showForm(Model model) {
        HtplUserDetails userDetails = new HtplUserDetails();
        model.addAttribute("userDetails", userDetails);
        return "signup";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String save(@ModelAttribute(value = "userDetails") HtplUserDetails userDetails) {
        this.userService.save(userDetails);
        return "index";
    }
}