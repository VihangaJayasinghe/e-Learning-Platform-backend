package com.Learn.ELP_backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "courses")
public class Course {
     @Id
    private String id;
    
    private String courseTitle; // Mechanics Mastery
    private String description;
    private String subject; // Physics
    private String topic; //  Mechanics, Electromagnetism
    private String level; // Beginner, Intermediate, Advanced
    private String instructorId;
    private String instructorName;
    
    @Builder.Default
    private List<String> videoIds = new ArrayList<>();
    
    @Builder.Default
    private List<String> documentIds = new ArrayList<>();
    
    @Builder.Default
    private List<String> quizIds = new ArrayList<>();
    
    private Integer estimatedHours;
    private Integer totalLessons;
    
    @Builder.Default
    private Double averageRating = 0.0;
    
    @Builder.Default
    private Integer totalReviews = 0;
    
    private Double price;
    private Boolean isFree;
    
    @Builder.Default
    private List<String> tags = new ArrayList<>();
    
    private String thumbnailUrl;
    private String previewVideoUrl;
    
    @Builder.Default
    private Boolean isPublished = false;
    
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
    private LocalDateTime publishedAt;
}
