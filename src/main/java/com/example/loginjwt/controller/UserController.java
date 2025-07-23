package com.example.loginjwt.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.loginjwt.dto.UserDetailsRequest;
import com.example.loginjwt.dto.UserDetailsResponse;
import com.example.loginjwt.model.UserDetails;
import com.example.loginjwt.service.JwtService;
import com.example.loginjwt.service.UserDetailsService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@SecurityRequirement(name = "BearerAuth")
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
                        .body("Kullanıcıya ait detay bilgisi bulunamadı.");
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

    @PostMapping("/details")
    public ResponseEntity<?> createUserDetails(@RequestBody @Valid UserDetailsRequest request,
                                               HttpServletRequest httpRequest) {
        try {
            String authHeader = httpRequest.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Eksik veya geçersiz Authorization header.");
            }

            String token = authHeader.substring(7);
            Long userId = jwtService.extractUserId(token);

            if (userDetailsService.getDetailsByUserId(userId).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Bu kullanıcı için zaten detay kaydı var.");
            }

            UserDetails created = userDetailsService.createUserDetails(userId, request);

            UserDetailsResponse response = new UserDetailsResponse();
            response.setAddress(created.getAddress());
            response.setPhone(created.getPhone());
            response.setBirthDate(created.getBirthDate());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Bir hata oluştu: " + e.getMessage());
        }
    }

    @PutMapping("/details")
    public ResponseEntity<?> updateUserDetails(@RequestBody @Valid UserDetailsRequest request,
                                            HttpServletRequest httpRequest) {
        try {
            String authHeader = httpRequest.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Eksik veya geçersiz Authorization header.");
            }

            String token = authHeader.substring(7);
            Long userId = jwtService.extractUserId(token);

            Optional<UserDetails> userDetailsOpt = userDetailsService.getDetailsByUserId(userId);

            if (userDetailsOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Kullanıcı detay kaydı bulunamadı.");
            }

            UserDetails updatedDetails = userDetailsService.updateUserDetails(userId, request);

            UserDetailsResponse response = new UserDetailsResponse();
            response.setAddress(updatedDetails.getAddress());
            response.setPhone(updatedDetails.getPhone());
            response.setBirthDate(updatedDetails.getBirthDate());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Bir hata oluştu: " + e.getMessage());
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/details")
    public ResponseEntity<?> deleteUserDetails(HttpServletRequest httpRequest) {
        try {
            String authHeader = httpRequest.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Eksik veya geçersiz Authorization header.");
            }

            String token = authHeader.substring(7);
            Long userId = jwtService.extractUserId(token);

            boolean deleted = userDetailsService.deleteUserDetails(userId);

            if (!deleted) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Kullanıcı detay kaydı bulunamadı.");
            }

            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Bir hata oluştu: " + e.getMessage());
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
@DeleteMapping("/details/{userId}")
public ResponseEntity<?> deleteAnyUserDetails(@PathVariable Long userId) {
    boolean deleted = userDetailsService.deleteUserDetailsByAdmin(userId);

    if (!deleted) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Belirtilen kullanıcıya ait detay kaydı bulunamadı.");
    }

    return ResponseEntity.noContent().build();
}


}
