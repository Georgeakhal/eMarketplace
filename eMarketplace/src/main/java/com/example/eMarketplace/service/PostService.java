package com.example.eMarketplace.service;

import com.example.eMarketplace.model.Post;
import com.example.eMarketplace.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.List;

@AllArgsConstructor
@Service
public class PostService {
    private final PostRepository repository;

    public List<Post> getAll(){
        return repository.findAll();
    }

    public Page<Post> getAllByTimeDesc(Pageable pageable){
        return repository.findAllByOrderBySubmissionTimeDesc(pageable);
    }

    public Post getModel(String id){
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No Shop found with id: " + id));
    }

    public Post save(Post post){
        return repository.save(post);
    }

    public void update(String id, Post post) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("No Shop found with id: " + id);
        }
        repository.save(post);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}
