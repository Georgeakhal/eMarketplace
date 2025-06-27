package com.example.eMarketplace.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LoginDto {
    private String username;
    private String password;
    private String email;
}