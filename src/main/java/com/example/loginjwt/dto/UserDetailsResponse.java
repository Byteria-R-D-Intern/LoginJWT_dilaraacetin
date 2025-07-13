package com.example.loginjwt.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UserDetailsResponse {
    private String address;
    private String phone;
    private LocalDate birthDate;
}
