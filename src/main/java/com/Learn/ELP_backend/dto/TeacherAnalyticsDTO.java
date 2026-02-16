package com.Learn.ELP_backend.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherAnalyticsDTO {
    private Double totalEarnings;
    private Long totalStudents;
    private Long totalCourses;
    private Double averageRating;
    private List<RecentEnrollmentDTO> recentEnrollments;
}
