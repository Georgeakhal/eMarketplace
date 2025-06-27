package com.example.eMarketplace.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDto {
    private String username;
    private String email;
    private String password;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
}