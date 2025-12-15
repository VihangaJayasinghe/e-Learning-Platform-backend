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
        @SuppressWarnings("unchecked")
        Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), 
            ObjectUtils.asMap(
                "resource_type", "video",
                "folder", "elearning-videos"
            ));
        
        return (String) uploadResult.get("secure_url");
    }

    public String uploadRaw(MultipartFile file) throws IOException {
        @SuppressWarnings("unchecked")
        Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(),
            ObjectUtils.asMap(
                "resource_type", "raw",
                "folder", "elearning-documents"
            ));
        return (String) uploadResult.get("secure_url");
    }
    
    public void deleteVideo(String fileUrl) {
        try {
            String publicId = extractPublicIdFromUrl(fileUrl);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> result = cloudinary.uploader().destroy(publicId, 
                ObjectUtils.asMap(
                    "resource_type", "video"
                ));
            
            if (!"ok".equals(result.get("result"))) {
                throw new RuntimeException("Cloudinary deletion failed: " + result.get("result"));
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Cloudinary deletion failed: " + e.getMessage());
        }
    }

        public void deleteRaw(String fileUrl) {
        deleteFromCloudinary(fileUrl, "raw");
    }

     private void deleteFromCloudinary(String fileUrl, String resourceType) {
        try {
            String publicId = extractPublicIdFromUrl(fileUrl);

            @SuppressWarnings("unchecked")
            Map<String, Object> result = cloudinary.uploader().destroy(publicId,
                    ObjectUtils.asMap(
                            "resource_type", resourceType
                    ));

            if (!"ok".equals(result.get("result"))) {
                throw new RuntimeException("Cloudinary deletion failed: " + result.get("result"));
            }

        } catch (Exception e) {
            throw new RuntimeException("Cloudinary deletion failed: " + e.getMessage());
        }
    }

    private String extractPublicIdFromUrl(String fileUrl) {
        try {
            String[] parts = fileUrl.split("/upload/");
            if (parts.length < 2) {
                throw new RuntimeException("Invalid Cloudinary URL format");
            }
            
            String pathAfterUpload = parts[1];
            
            if (pathAfterUpload.startsWith("v")) {
                pathAfterUpload = pathAfterUpload.substring(pathAfterUpload.indexOf("/") + 1);
            }
            
            return pathAfterUpload.split("\\.")[0];
            
        } catch (Exception e) {
            throw new RuntimeException("Invalid Cloudinary URL format: " + e.getMessage());
        }
    }
}