package com.Learn.ELP_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SystemStatsDTO {
    private long totalUsers;
    private long totalCourses;
    private long totalVideos;
    private long totalInstructors;
    private long totalStudents;
}
