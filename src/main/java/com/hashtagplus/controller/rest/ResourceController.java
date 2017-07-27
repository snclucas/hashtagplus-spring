package com.hashtagplus.controller.rest;

import com.hashtagplus.model.repo.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceController {

    @Autowired
    private UserDetailsRepository userRepository;

    //@Secured({"ROLE_ADMIN"})
    @RequestMapping("/resource")
    public String home(@AuthenticationPrincipal UserDetails userDetails) {
        return userDetails.getAuthorities().toString();
    }
}