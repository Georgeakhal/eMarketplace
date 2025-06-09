package com.example.eMarketplace.controller;

import com.example.eMarketplace.dto.PostDto;
import com.example.eMarketplace.model.Post;
import com.example.eMarketplace.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.SpringCacheAnnotationParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/product")
public class MarketController {
    private final PostService postService;
    private static final String INDEX_HTML_PATH = "eMarketplace/src/main/webapp/index.html";

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAll(){
        return ResponseEntity.ok(postService.getAll());
    }


    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Post> create(@RequestBody PostDto dto) {
        String id = UUID.randomUUID().toString();


        Post post = new Post();
        post.setId(id);
        post.setName(dto.getName());
        post.setPrice(dto.getPrice());
        post.setDescription(dto.getDescription());
        post.setPhotoUrl(dto.getPhotoUrl());

        LocalDateTime time = LocalDateTime.now();
        post.setSubmissionTime(Date.valueOf(time.toLocalDate()));

        return ResponseEntity.status(HttpStatus.CREATED).body(postService.save(post));
    }


}
