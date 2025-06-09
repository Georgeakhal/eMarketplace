package com.example.eMarketplace.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class PostDto {
    private String name;
    private int price;
    private String description;
    private String photoUrl;
}
