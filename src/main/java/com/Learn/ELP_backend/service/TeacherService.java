package com.Learn.ELP_backend.service;

import com.Learn.ELP_backend.dto.TeacherAnalyticsDTO;

public interface TeacherService {
    TeacherAnalyticsDTO getAnalytics(String username);
}
