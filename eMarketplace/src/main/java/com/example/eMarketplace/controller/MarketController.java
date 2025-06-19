package com.example.eMarketplace.controller;

import com.example.eMarketplace.dto.PostDto;
import com.example.eMarketplace.model.Post;
import com.example.eMarketplace.service.PostService;
import com.example.eMarketplace.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.SpringCacheAnnotationParser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;


import org.springframework.data.domain.Pageable;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/product")
public class MarketController {
    private final PostService postService;
    private final UserService userService;
    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping
    public ResponseEntity<Page<Post>> getAll( @RequestParam int page,
                                              @RequestParam int size,
                                              @RequestParam String sort)
    {
        Pageable pageable = PageRequest.of(page, size);

        if (sort.equals("dateAsc")){
            Page<Post> posts = postService.getAllByTimeAsc(pageable);
            return ResponseEntity.ok(posts);
        } else if (sort.equals("priceDesc")) {
            Page<Post> posts = postService.getAllByPriceDesc(pageable);
            return ResponseEntity.ok(posts);
        } else if (sort.equals("priceAsc")) {
            Page<Post> posts = postService.getAllByPriceAsc(pageable);
            return ResponseEntity.ok(posts);
        } else {
            Page<Post> posts = postService.getAllByTimeDesc(pageable);
            return ResponseEntity.ok(posts);
        }
    }
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Void> create(@RequestBody PostDto dto) {
        Post post = new Post();
        post.setId(UUID.randomUUID().toString());
        post.setName(dto.getName());
        post.setPrice(dto.getPrice());
        post.setDescription(dto.getDescription());
        post.setPhotoUrl(dto.getPhotoUrl());
        post.setUser(userService.getById(dto.getUserId()));
        post.setSubmissionTime(Timestamp.valueOf(LocalDateTime.now()));

        postService.save(post);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}
