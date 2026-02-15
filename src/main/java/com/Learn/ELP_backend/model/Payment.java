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
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "payments")
public class Payment {
    @Id
    private String id;
    
    @DBRef
    private User student;
    
    private String classId;
    
    // Format: "YYYY-MM" (e.g., "2025-11")
    private String yearMonth;
    
    private Double amount;
    
    @Builder.Default
    private String currency = "usd";
    
    private String stripePaymentIntentId;
    
    @Builder.Default
    private PaymentStatus status = PaymentStatus.PENDING;
    
    @Builder.Default
    private LocalDateTime paymentDate = LocalDateTime.now();
    
    // Optional: Reference to the course/class name for easier display without joins
    private String className; 
}
