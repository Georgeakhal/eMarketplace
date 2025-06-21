package com.example.eMarketplace.repository;

import com.example.eMarketplace.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {
    Page<Post> findAllByOrderBySubmissionTimeDesc(Pageable pageable);
    Page<Post> findAllByOrderBySubmissionTimeAsc(Pageable pageable);
    Page<Post> findAllByOrderByPriceAsc(Pageable pageable);
    Page<Post> findAllByOrderByPriceDesc(Pageable pageable);



}
