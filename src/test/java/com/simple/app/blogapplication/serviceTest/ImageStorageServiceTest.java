package com.simple.app.blogapplication.serviceTest;

import static org.mockito.Mockito.doReturn;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.simple.app.blogapplication.model.PostImageDao;
import com.simple.app.blogapplication.repository.ImageRepository;
import com.simple.app.blogapplication.service.ImageStorageService;

@SpringBootTest
public class ImageStorageServiceTest {
	
	@Autowired
	private ImageStorageService ImageStorageServiceTest;

	@MockBean
	private ImageRepository imageRepository;

	@Test
	void testFindByIdOnImage() {
		
		PostImageDao imageDao = new PostImageDao();
		imageDao.setId("12345");
		imageDao.setName("image");

		doReturn(Optional.of(imageDao)).when(imageRepository).findById("12345");

		PostImageDao returnImage = ImageStorageServiceTest.getImage("12345");
		
		Assertions.assertSame(returnImage.getId(), "12345");
	}

}
