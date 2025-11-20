package com.Learn.ELP_backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.Data;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryStorageService {

    @Autowired
    private Cloudinary cloudinary;

    public VideoUploadResult uploadVideoWithABS(MultipartFile file) throws IOException {
        try {
            // Simple upload without any complex transformations
            @SuppressWarnings("unchecked")
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), 
                ObjectUtils.asMap(
                    "resource_type", "video",
                    "folder", "elearning-videos"
                ));

            String secureUrl = (String) uploadResult.get("secure_url");
            String publicId = (String) uploadResult.get("public_id");

            System.out.println("✅ Video upload successful!");
            System.out.println("Public ID: " + publicId);
            System.out.println("MP4 URL: " + secureUrl);

            // Generate HLS URL using the simple manual approach
            String hlsUrl = generateSimpleHLSURL(secureUrl);

            return VideoUploadResult.builder()
                    .secureUrl(secureUrl)
                    .playbackUrl(hlsUrl)
                    .jobId(publicId)
                    .build();

        } catch (Exception e) {
            System.err.println("❌ Cloudinary upload error: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("Cloudinary upload failed: " + e.getMessage(), e);
        }
    }

    private String generateSimpleHLSURL(String secureUrl) {
        // Manually construct HLS URL by inserting sp_auto and changing extension
        // Original: https://res.cloudinary.com/cloudname/video/upload/v1234567/folder/video.mp4
        // HLS:      https://res.cloudinary.com/cloudname/video/upload/sp_auto/v1234567/folder/video.m3u8
        String hlsUrl = secureUrl.replace("/upload/", "/upload/sp_auto/").replace(".mp4", ".m3u8");
        
        System.out.println("Generated HLS URL: " + hlsUrl);
        return hlsUrl;
    }

    // Alternative method with Transformation object (if needed later)
    public VideoUploadResult uploadVideoWithTransformation(MultipartFile file) throws IOException {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), 
                ObjectUtils.asMap(
                    "resource_type", "video",
                    "folder", "elearning-videos"
                ));

            String secureUrl = (String) uploadResult.get("secure_url");
            String publicId = (String) uploadResult.get("public_id");

            System.out.println("✅ Transformation upload successful!");

            // Generate HLS URL using Transformation object
            @SuppressWarnings("rawtypes")
            com.cloudinary.Transformation transformation = new com.cloudinary.Transformation()
                .streamingProfile("sp_auto");

            String hlsUrl = cloudinary.url()
                .resourceType("video")
                .publicId(publicId)
                .format("m3u8")
                .transformation(transformation)
                .generate();

            System.out.println("Generated HLS URL with transformation: " + hlsUrl);

            return VideoUploadResult.builder()
                    .secureUrl(secureUrl)
                    .playbackUrl(hlsUrl)
                    .jobId(publicId)
                    .build();

        } catch (Exception e) {
            System.err.println("❌ Transformation upload error: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("Transformation upload failed: " + e.getMessage(), e);
        }
    }
    
    public void deleteVideo(String fileUrl) {
        try {
            String publicId = extractPublicIdFromUrl(fileUrl);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> result = cloudinary.uploader().destroy(publicId, 
                ObjectUtils.asMap("resource_type", "video"));
            
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

    @Data
    @Builder
    public static class VideoUploadResult {
        private String secureUrl;
        private String playbackUrl;
        private String jobId;
    }
}