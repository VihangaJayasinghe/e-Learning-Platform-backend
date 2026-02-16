package com.Learn.ELP_backend.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecentEnrollmentDTO {
    private String studentName;
    private String courseTitle;
    private LocalDateTime enrolledAt;
}
