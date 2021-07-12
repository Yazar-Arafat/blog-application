package com.simple.app.blogapplication.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simple.app.blogapplication.dto.DeleteResponseMessage;
import com.simple.app.blogapplication.dto.PostDto;
import com.simple.app.blogapplication.service.PostService;

@RestController
@RequestMapping("/api/posts")
public class PostController {

	@Autowired
	private PostService postService;

	@PostMapping("/create/{name}")
	public ResponseEntity<?> createPost(@RequestBody PostDto postDto, @PathVariable String name) {
		postService.createPost(postDto, name);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/all")
	public ResponseEntity<List<PostDto>> showAllPosts() {
		return new ResponseEntity<>(postService.showAllPosts(), HttpStatus.OK);
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<PostDto> getSinglePost(@PathVariable @RequestBody Long id) {
		return new ResponseEntity<>(postService.readSinglePost(id), HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<DeleteResponseMessage> deleteSinglePost(@PathVariable @RequestBody Long id) {
		String message = "";
		try {
			postService.deleteSinglePost(id);
			message = "post deleted successfully: ";
			return ResponseEntity.status(HttpStatus.OK).body(new DeleteResponseMessage(message));
		} catch (Exception e) {
			message = "Could not delete the post: " + id + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new DeleteResponseMessage(message));
		}
	}
}
