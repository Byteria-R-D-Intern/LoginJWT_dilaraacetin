package com.example.loginjwt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.example.loginjwt.dto.AuthenticationRequest;
import com.example.loginjwt.dto.AuthenticationResponse;
import com.example.loginjwt.model.User;
import com.example.loginjwt.repository.UserRepository;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı: " + request.getEmail()));

            String token = jwtService.generateToken(user);

            return new AuthenticationResponse(token);

        } catch (AuthenticationException e) {
            throw new RuntimeException("Geçersiz e-posta veya şifre.");
        }
    }
}
