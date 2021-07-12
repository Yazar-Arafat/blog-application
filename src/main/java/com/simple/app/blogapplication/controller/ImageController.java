package com.simple.app.blogapplication.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.simple.app.blogapplication.dto.ImageResponse;
import com.simple.app.blogapplication.dto.ImageResponseMessage;
import com.simple.app.blogapplication.model.PostImageDao;
import com.simple.app.blogapplication.service.ImageStorageService;

@RestController()
@RequestMapping("/api")
public class ImageController {

    @Autowired
    private ImageStorageService imageStorageService;

    @PostMapping(value="/image/upload")
    public ResponseEntity<ImageResponseMessage> uploadFile(@RequestPart(name = "file", required = false) MultipartFile file) {
        String message = "";
        try {
            imageStorageService.store(file);

            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ImageResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ImageResponseMessage(message));
        }
    }

    @GetMapping("/images")
    public ResponseEntity<List<ImageResponse>> getListFiles() {
        List<ImageResponse> files = imageStorageService.getAllFiles().map(dbFile -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/image/")
                    .path(dbFile.getId())
                    .toUriString();

            return new ImageResponse(
                    dbFile.getName(),
                    fileDownloadUri,
                    dbFile.getType(),
                    dbFile.getData().length);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable String id) {
        PostImageDao image = imageStorageService.getImage(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getName() + "\"")
                .body(image.getData());
    }
}
