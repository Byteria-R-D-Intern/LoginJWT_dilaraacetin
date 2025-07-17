package com.example.loginjwt.controller;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "BearerAuth")
@RestController
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/ping")
    public ResponseEntity<String> adminPing() {
        return ResponseEntity.ok("Admin endpoint'e erişiminiz var!");
    }
}
