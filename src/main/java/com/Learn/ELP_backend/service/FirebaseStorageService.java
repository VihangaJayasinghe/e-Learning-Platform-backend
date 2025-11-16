package com.Learn.ELP_backend.service;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class FirebaseStorageService {

    //@Autowired
    private Storage firebaseStorage;

    // Replace with your actual bucket name from Firebase console
    private final String BUCKET_NAME = "elearning-platform-1e72c.appspot.com";

    public String uploadVideo(MultipartFile file) throws IOException {
        // Generate unique filename to avoid conflicts
        String fileName = "videos/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
        
        // Upload to Firebase Storage
        BlobInfo blobInfo = BlobInfo.newBuilder(BUCKET_NAME, fileName)
                .setContentType(file.getContentType())
                .build();
        
        firebaseStorage.create(blobInfo, file.getBytes());
        
        // Generate public URL for accessing the video
        return String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media", 
                           BUCKET_NAME, 
                           URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }
    
    public void deleteVideo(String fileUrl) {
        try {
            // Extract file path from URL
            String fileName = extractFileNameFromUrl(fileUrl);
            firebaseStorage.delete(BUCKET_NAME, fileName);
        } catch (Exception e) {
            System.out.println("Error deleting file from Firebase: " + e.getMessage());
        }
    }
    
    private String extractFileNameFromUrl(String fileUrl) {
        // Extract "videos/xxx-filename" from the full URL
        String baseUrl = "https://firebasestorage.googleapis.com/v0/b/" + BUCKET_NAME + "/o/";
        String encodedFileName = fileUrl.replace(baseUrl, "").split("\\?")[0];
        return URLDecoder.decode(encodedFileName, StandardCharsets.UTF_8);
    }
}