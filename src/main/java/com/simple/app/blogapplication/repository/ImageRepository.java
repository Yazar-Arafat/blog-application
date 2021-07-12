package com.simple.app.blogapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simple.app.blogapplication.model.PostImageDao;

@Repository
public interface ImageRepository extends JpaRepository<PostImageDao, String> {
    PostImageDao findByName(String name);
    boolean existsByName(String name);
}
