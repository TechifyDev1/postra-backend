package com.qudus.postra.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(@Value("${cloudinary.cloud_name}") String cloudName,
            @Value("${cloudinary.api_key}") String apiKey, @Value("${cloudinary.api_secret}") String apiSecrete) {
        Map<String, String> config = new HashMap<String, String>();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecrete);
        this.cloudinary = new Cloudinary(config);
    }

    public Map<Object, Object> uploadAndReturnUrl(MultipartFile file) {
        try {
            Map<Object, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            System.out.println(uploadResult);
            return uploadResult;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public boolean delete(String publicId) {
        try {
            Map<?, ?> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            System.out.println(result);
            return result.get("result").equals("ok");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}