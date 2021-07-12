package com.simple.app.blogapplication.service;

import static java.util.stream.Collectors.toList;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simple.app.blogapplication.dto.PostDto;
import com.simple.app.blogapplication.exception.PostNotFoundException;
import com.simple.app.blogapplication.model.Post;
import com.simple.app.blogapplication.repository.PostRepository;

@Service
public class PostService {

	@Autowired
	private AuthService authService;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private ImageStorageService imageStorageService;

	@Transactional
	public List<PostDto> showAllPosts() {
		List<Post> posts = postRepository.findAll();
		return posts.stream().map(this::mapFromPostToDto).collect(toList());
	}

	@Transactional
	public void createPost(PostDto postDto, String name) {
		Post post = mapFromDtoToPost(postDto, name);
		postRepository.save(post);
	}

	@Transactional
	public PostDto readSinglePost(Long id) {
		Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("For id " + id));
		return mapFromPostToDto(post);
	}

	@Transactional
	public void deleteSinglePost(Long id) {
		try {
			PostDto result = readSinglePost(id);
			if (result.getImageUrl() != null && !result.getImageUrl().trim().isEmpty()) {
				imageStorageService.deleteById(result.getImageUrl());
			}
			postRepository.deleteById(id);
		} catch (PostNotFoundException e) {

		}
	}

	private PostDto mapFromPostToDto(Post post) {
		PostDto postDto = new PostDto();
		postDto.setId(post.getId());
		postDto.setTitle(post.getTitle());
		postDto.setContent(post.getContent());
		postDto.setUsername(post.getUsername());
		postDto.setImageUrl(post.getImageUrl());
		return postDto;
	}

	private Post mapFromDtoToPost(PostDto postDto, String name) {
		Post post = new Post();
		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		User loggedInUser = authService.getCurrentUser()
				.orElseThrow(() -> new IllegalArgumentException("User Not Found"));
		post.setCreatedOn(Instant.now());
		post.setUsername(loggedInUser.getUsername());
		post.setUpdatedOn(Instant.now());
		boolean exists = imageStorageService.getExistsByName(name);
		if (exists) {
			post.setImageUrl(imageStorageService.getImageByName(name).getId());
		}
		return post;
	}
}
