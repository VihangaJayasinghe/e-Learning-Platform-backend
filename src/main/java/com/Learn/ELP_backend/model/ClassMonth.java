package com.Learn.ELP_backend.model;

import lombok.Data;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ClassMonth {
    private String yearMonth;        // String format: "2024-03"
    private String displayName;      // "March 2024"
    
    @Builder.Default
    private List<String> videoIds = new ArrayList<>();
    
    @Builder.Default
    private boolean isReleased = false;
    
    private LocalDateTime releaseDate;
}