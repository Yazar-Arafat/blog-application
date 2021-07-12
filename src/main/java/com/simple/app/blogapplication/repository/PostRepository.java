package com.simple.app.blogapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simple.app.blogapplication.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

}
