package com.example.loginjwt.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserDetailsRequest {

    @NotBlank(message = "Adres boş bırakılamaz.")
    private String address;

    @Pattern(regexp = "^\\d{10}$", message = "Telefon numarası 10 haneli ve sadece sayısal olmalıdır.")
    private String phone;

    @Past(message = "Doğum tarihi geçmiş bir tarih olmalıdır.")
    @NotNull(message = "Doğum tarihi boş bırakılamaz.")
    private LocalDate birthDate;
}
