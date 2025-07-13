package com.example.loginjwt.controller;

import com.example.loginjwt.dto.UserDetailsResponse;
import com.example.loginjwt.model.UserDetails;
import com.example.loginjwt.service.JwtService;
import com.example.loginjwt.service.UserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @GetMapping("/details")
    public ResponseEntity<?> getUserDetails(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Eksik veya geçersiz Authorization header.");
            }

            String token = authHeader.substring(7);

            Long userId = jwtService.extractUserId(token);

            Optional<UserDetails> userDetailsOpt = userDetailsService.getDetailsByUserId(userId);

            if (userDetailsOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Kullanıcı detay bilgisi bulunamadı.");
            }

            UserDetails details = userDetailsOpt.get();

            UserDetailsResponse response = new UserDetailsResponse();
            response.setAddress(details.getAddress());
            response.setPhone(details.getPhone());
            response.setBirthDate(details.getBirthDate());

            return ResponseEntity.ok(response);

        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token süresi dolmuş.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Geçersiz token veya erişim reddedildi.");
        }
    }
}
