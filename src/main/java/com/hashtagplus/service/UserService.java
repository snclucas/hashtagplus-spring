package com.hashtagplus.service;

import com.hashtagplus.model.HtplUserDetails;
import com.hashtagplus.model.repo.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserService {

    @Autowired
    private UserDetailsRepository userRepository;

    @Autowired
    public UserService(UserDetailsRepository userDetailsRepository) {
        this.userRepository = userDetailsRepository;
    }

    public List<HtplUserDetails> getAllUsers() {
        return userRepository.findAll();
    }

    public HtplUserDetails getUserById(String id) {
        return userRepository.findOne(id);
    }

    public HtplUserDetails getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public HtplUserDetails getUserByToken(String token) {
        return userRepository.findByToken(token);
    }

    public HtplUserDetails save(HtplUserDetails customUserDetails) {
        return this.userRepository.insert(customUserDetails);
    }

}