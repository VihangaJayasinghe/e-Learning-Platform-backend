package com.Learn.ELP_backend.model;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "documents")
public class Documents {
    @Id
    private String Id;

    private String documentName;
     private String description;
    private String fileName;
    private Long fileSize;
    private String contentType;
    private String cloudinaryUrl;
    private String uploadedBy;
    private String classId;

    @Builder.Default
     private LocalDateTime uploadDate = LocalDateTime.now();
}
