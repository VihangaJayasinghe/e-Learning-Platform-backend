package com.Learn.ELP_backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

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
    private String firebaseUrl; // Original MP4 URL
    private String playbackUrl; // HLS URL with sp_auto
    private String fileName;
    private Long fileSize;
    private String contentType;
    
    @Builder.Default
    private LocalDateTime uploadDate = LocalDateTime.now();
    
    private String uploadedBy;
    private String courseId;
    
    // ABS-specific fields
    private VideoStatus status;
    private String processingJobId;
    
    public enum VideoStatus {
        UPLOADED, PROCESSING, READY, FAILED
    }
}