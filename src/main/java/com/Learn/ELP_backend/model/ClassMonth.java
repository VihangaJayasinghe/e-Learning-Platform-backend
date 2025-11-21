package com.Learn.ELP_backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClassMonth {
    private String yearMonth;        // String format: "2024-03"
    private String displayName;      // "March 2024"
    
    @Builder.Default
    private List<String> videoIds = new ArrayList<>();
    
    @Builder.Default
    private boolean isReleased = false;
    
    private LocalDateTime releaseDate;

    // quizes for month
    @Builder.Default
    private List<Quiz> quizzes = new ArrayList<>();

}