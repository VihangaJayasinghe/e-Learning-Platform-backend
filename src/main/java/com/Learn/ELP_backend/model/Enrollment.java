package com.Learn.ELP_backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "enrollments")
public class Enrollment {
    @Id
    private String id;

    @DBRef
    private User student;

    @DBRef
    private Class classEnrolled;

    private LocalDateTime enrollmentDate;

    private EnrollmentStatus status;

    private PaymentStatus paymentStatus;
}
