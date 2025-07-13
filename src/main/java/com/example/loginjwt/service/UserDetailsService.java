package com.example.loginjwt.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.loginjwt.model.UserDetails;
import com.example.loginjwt.repository.UserDetailsRepository;

@Service
public class UserDetailsService {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    public Optional<UserDetails> getDetailsByUserId(Long userId) {
        return userDetailsRepository.findByUserId(userId);
    }
}
