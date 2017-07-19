package com.hashtagplus.service;

import com.hashtagplus.model.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserAuthService implements UserDetailsService {

    private UserDetailsRepository userDetailsRepository;

    @Autowired
    public UserAuthService(UserDetailsRepository userDetailsRepository) {

        this.userDetailsRepository = userDetailsRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return this.userDetailsRepository.findByUsername(username);
    }
}