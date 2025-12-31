package com.Learn.ELP_backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "reviews")
public class Review {
     @Id
    private String id;

    private String targetId; // Course ID or Class ID
    private ReviewTargetType targetType; // COURSE or CLASS

    private String studentId;
    private String studentName;

    private Integer rating;
    private String title;
    private String comment;

    private String instructorId; // For filtering by instructor
    
    // Status (for moderation)
    @Builder.Default
    private Boolean isApproved = true;
    
    @Builder.Default
    private Integer helpfulVotes = 0;
    
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
    private LocalDateTime updatedAt;

    public enum ReviewTargetType {
        COURSE, CLASS
    }
}
