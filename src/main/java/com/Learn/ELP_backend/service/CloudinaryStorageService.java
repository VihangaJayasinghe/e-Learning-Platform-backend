package com.Learn.ELP_backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryStorageService {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadVideo(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), 
            ObjectUtils.asMap(
                "resource_type", "video",
                "folder", "elearning-videos"
            ));
        
        return (String) uploadResult.get("secure_url");
    }
    
    public void deleteVideo(String fileUrl) {
        try {
            // Extract public ID from URL
            String publicId = extractPublicIdFromUrl(fileUrl);
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (Exception e) {
            System.out.println("Error deleting video: " + e.getMessage());
        }
    }
    
    private String extractPublicIdFromUrl(String fileUrl) {
        // Extract public ID from Cloudinary URL
        // URL format: https://res.cloudinary.com/cloud-name/video/upload/v1234567/public-id.mp4
        String[] parts = fileUrl.split("/");
        String fileNameWithExtension = parts[parts.length - 1];
        return "elearning-videos/" + fileNameWithExtension.split("\\.")[0];
    }
}