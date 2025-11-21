package com.Learn.ELP_backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.CompoundIndex;

import java.time.YearMonth;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "classes")
@CompoundIndex(name = "instructor_status", def = "{'instructorId': 1, 'status': 1}")
public class Class {
    @Id
    private String id;
    
    private String className;
    private String description;
    private String instructorId;
    private Double monthlyPrice;
    
    private String startMonth;        // String format: "2024-03"
    private Integer durationMonths;
    
    @Builder.Default
    private List<ClassMonth> months = new ArrayList<>();
    
    @Builder.Default
    private LocalDateTime createdDate = LocalDateTime.now();
    
    @Builder.Default
    private ClassStatus status = ClassStatus.DRAFT;
    
    // Auto-create months based on duration
    public void initializeMonths() {
        this.months = new ArrayList<>();
        YearMonth start = YearMonth.parse(this.startMonth);
        
        for (int i = 0; i < durationMonths; i++) {
            YearMonth month = start.plusMonths(i);
            months.add(ClassMonth.builder()
                .yearMonth(month.toString())
                .displayName(month.getMonth().toString() + " " + month.getYear())
                .videoIds(new ArrayList<>())
                .isReleased(false)
                .releaseDate(null)
                .build());
        }
    }
    
    // Calculate end month
    public String getEndMonth() {
        YearMonth start = YearMonth.parse(this.startMonth);
        YearMonth end = start.plusMonths(durationMonths - 1);
        return end.toString();
    }
    
    // Extend class duration
    public void extendDuration(int additionalMonths) {
        YearMonth currentEnd = YearMonth.parse(getEndMonth());
        for (int i = 1; i <= additionalMonths; i++) {
            YearMonth newMonth = currentEnd.plusMonths(i);
            months.add(ClassMonth.builder()
                .yearMonth(newMonth.toString())
                .displayName(newMonth.getMonth().toString() + " " + newMonth.getYear())
                .videoIds(new ArrayList<>())
                .isReleased(false)
                .releaseDate(null)
                .build());
        }
        this.durationMonths += additionalMonths;
    }
    
    // Find month by string
    public ClassMonth findMonth(String yearMonth) {
        return months.stream()
                .filter(month -> month.getYearMonth().equals(yearMonth))
                .findFirst()
                .orElse(null);
    }

    // Find quiz
    public Quiz findQuiz(String yearMonth, String quizId) {
        ClassMonth month = findMonth(yearMonth);
        if (month == null) return null;

        return month.getQuizzes()
            .stream()
            .filter(q -> q.getId().equals(quizId))
            .findFirst()
            .orElse(null);
    }
}