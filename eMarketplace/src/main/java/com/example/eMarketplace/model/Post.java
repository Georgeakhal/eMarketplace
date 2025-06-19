package com.example.eMarketplace.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "post")
@Getter
@Setter
public class Post {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private int price;

    @Column(name = "description")
    private String description;

    @Column(name = "submission_time")
    private Timestamp submissionTime;

    @Column(name = "photo_url")
    private String photoUrl;

    @OneToOne
    @JoinColumn(name = "user_Id", referencedColumnName = "id")
    private User user;

}
