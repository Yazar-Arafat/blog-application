package com.simple.app.blogapplication.serviceTest;

import static org.mockito.Mockito.doReturn;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.simple.app.blogapplication.dto.PostDto;
import com.simple.app.blogapplication.model.Post;
import com.simple.app.blogapplication.model.PostImageDao;
import com.simple.app.blogapplication.repository.ImageRepository;
import com.simple.app.blogapplication.repository.PostRepository;
import com.simple.app.blogapplication.service.PostService;

@SpringBootTest
public class PostServiceTest {

	@Autowired
	private PostService postService;

	@MockBean
	private PostRepository repository;

	@MockBean
	private ImageRepository imageRepository;

	@Test
	@DisplayName("Test findById Success")
	void testFindById() {
		Post postDao = new Post();
		postDao.setId((long) 1);
		postDao.setImageUrl("9821398");
		postDao.setContent("sample text");
		postDao.setTitle("title");
		postDao.setUsername("yazar");
		doReturn(Optional.of(postDao)).when(repository).findById((long) 1);

		PostDto returnenPost = postService.readSinglePost((long) 1);

		Assertions.assertSame(returnenPost.getTitle(), "title");
	}

	@Test
	@DisplayName("Test Delete Post Success")
	void testDeletePost() {
		Post postDao = new Post();
		postDao.setId((long) 1);
		postDao.setImageUrl("12345");
		postDao.setContent("sample text");
		postDao.setTitle("title");
		postDao.setUsername("yazar");

		PostImageDao imageDao = new PostImageDao();
		imageDao.setId("12345");
		imageDao.setName("image");

		doReturn(Optional.of(imageDao)).when(imageRepository).findById("12345");
		doReturn(Optional.of(postDao)).when(repository).findById((long) 1);
		postService.deleteSinglePost((long) 1);
	}

}
