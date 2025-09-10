package com.qudus.postra.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.qudus.postra.service.CloudinaryService;

@RestController
@RequestMapping("/api")
public class UploadConroller {

    // cloudinary response dto
    public record CloudinaryResponse(String url, String public_id) {
    }

    private final CloudinaryService cloudinaryService;

    public UploadConroller(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping("/upload")
    public ResponseEntity<CloudinaryResponse> upload(@RequestParam("file") MultipartFile file) {
        Map<Object, Object> result = cloudinaryService.uploadAndReturnUrl(file);
        CloudinaryResponse response = new CloudinaryResponse((String) result.get("secure_url"),
                (String) result.get("public_id"));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam("publicId") String publicId) {
        System.out.println(publicId);
        boolean success = cloudinaryService.delete(publicId);
        if (success) {
            return ResponseEntity.ok("Deleted");
        } else {
            return ResponseEntity.badRequest().body("Unable to delete");
        }
    }
}
