package com.example.eMarketplace.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
@Getter
@Setter
@AllArgsConstructor
public class UserDto {
    private String username;
    private String email;
    private String password;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
}