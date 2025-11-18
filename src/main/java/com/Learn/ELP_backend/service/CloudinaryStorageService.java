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

    // REMOVE or RENAME the old uploadVideo method to avoid conflicts
    // Keep only the new ABS method
    
    public VideoUploadResult uploadVideoWithABS(MultipartFile file) throws IOException {
        try {
            // Step 1: Simple upload without any eager transformations
            @SuppressWarnings("unchecked")
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), 
                ObjectUtils.asMap(
                    "resource_type", "video",
                    "folder", "elearning-videos"
                    // Remove all eager and streaming_profile parameters for now
                ));

            String secureUrl = (String) uploadResult.get("secure_url");
            String publicId = (String) uploadResult.get("public_id");

            System.out.println("✅ Basic upload successful!");
            System.out.println("Public ID: " + publicId);
            System.out.println("Secure URL: " + secureUrl);

            // Step 2: Generate HLS URL manually (this doesn't trigger processing)
            String hlsUrl = generateHLSUrl(publicId);

            return VideoUploadResult.builder()
                    .secureUrl(secureUrl)
                    .playbackUrl(hlsUrl) // This will be the HLS URL
                    .jobId(publicId)
                    .build();

        } catch (Exception e) {
            System.err.println("❌ Cloudinary upload error: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("Cloudinary upload failed: " + e.getMessage(), e);
        }
    }

    private String generateHLSUrl(String publicId) {
        // Simple HLS URL - Cloudinary handles adaptive streaming automatically with m3u8 format
        String hlsUrl = cloudinary.url()
            .resourceType("video")
            .publicId(publicId)
            .format("m3u8") // This automatically enables adaptive streaming
            .generate();

        System.out.println("Generated HLS URL: " + hlsUrl);
        return hlsUrl;
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