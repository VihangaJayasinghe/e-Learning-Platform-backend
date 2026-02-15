package com.Learn.ELP_backend.service;

import com.Learn.ELP_backend.model.Payment;
import com.Learn.ELP_backend.model.User;

import java.util.List;

public interface PaymentService {
    Payment initiatePayment(User student, String classId, String yearMonth, Double amount);

    Payment completePayment(String paymentId);

    List<Payment> getStudentPayments(User student);

    boolean hasAccess(User student, String classId, String yearMonth);
}
