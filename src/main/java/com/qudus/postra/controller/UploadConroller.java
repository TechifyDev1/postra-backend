package com.qudus.postra.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.qudus.postra.service.CloudinaryService;

@RestController
@RequestMapping("/api")
public class UploadConroller {

    private final CloudinaryService cloudinaryService;

    public UploadConroller(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }
    
    @GetMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(cloudinaryService.uploadAndReturnUrl(file));
    }
}
