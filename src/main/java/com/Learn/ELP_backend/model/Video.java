package com.Learn.ELP_backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "videos")
public class Video {
    @Id
    private String id;
    
    private String videoName;
    private String description;
    private String firebaseUrl;
    private String fileName;
    private Long fileSize;
    private String contentType;

    // ABS-specific fields
    private String playbackUrl; // HLS master playlist URL
    private VideoStatus status;
    private String processingJobId; // Cloudinary async job ID
    private LocalDateTime processedDate;
    private List<VideoQuality> availableQualities;
    
    public enum VideoStatus {
        UPLOADED, PROCESSING, READY, FAILED
    }
    
    @Data
    @Builder
    public static class VideoQuality {
        private String quality;
        private String resolution;
        private Long bitrate;
    }
    
    @Builder.Default
    private LocalDateTime uploadDate = LocalDateTime.now();
    
    private String uploadedBy;
    @JsonIgnore
    private String courseId;
}