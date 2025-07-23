package com.example.loginjwt.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.loginjwt.dto.UserDetailsRequest;
import com.example.loginjwt.model.User;
import com.example.loginjwt.model.UserDetails;
import com.example.loginjwt.repository.UserDetailsRepository;
import com.example.loginjwt.repository.UserRepository;

@Service
public class UserDetailsService {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private UserRepository userRepository;

    public Optional<UserDetails> getDetailsByUserId(Long userId) {
        return userDetailsRepository.findByUserId(userId);
    }

    public UserDetails createUserDetails(Long userId, UserDetailsRequest request) {
        if (userDetailsRepository.findByUserId(userId).isPresent()) {
            throw new IllegalStateException("Kullanıcı detayları zaten mevcut.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı: " + userId));

        UserDetails details = new UserDetails();
        details.setUser(user);
        details.setAddress(request.getAddress());
        details.setPhone(request.getPhone());
        details.setBirthDate(request.getBirthDate());

        return userDetailsRepository.save(details);
    }

    public UserDetails updateUserDetails(Long userId, UserDetailsRequest request) {
        UserDetails details = userDetailsRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı detayları bulunamadı."));

        details.setAddress(request.getAddress());
        details.setPhone(request.getPhone());
        details.setBirthDate(request.getBirthDate());

        return userDetailsRepository.save(details);
    }

    public boolean deleteUserDetails(Long userId) {
        Optional<UserDetails> existing = userDetailsRepository.findByUserId(userId);
        if (existing.isPresent()) {
            userDetailsRepository.delete(existing.get());
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteUserDetailsByAdmin(Long userId) {
        Optional<UserDetails> details = userDetailsRepository.findByUserId(userId);
        if (details.isPresent()) {
            userDetailsRepository.delete(details.get());
            return true;
        }
        return false;
    }
}
