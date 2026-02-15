package com.Learn.ELP_backend.service.impl;

import com.Learn.ELP_backend.model.Payment;
import com.Learn.ELP_backend.model.PaymentStatus;
import com.Learn.ELP_backend.model.User;
import com.Learn.ELP_backend.repository.PaymentRepository;
import com.Learn.ELP_backend.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private com.Learn.ELP_backend.repository.ClassRepository classRepository;

    @Override
    public Payment initiatePayment(User student, String classId, String yearMonth, Double amount) {
        // Fetch class to get the name
        String className = classRepository.findById(classId)
                .map(com.Learn.ELP_backend.model.Class::getClassName)
                .orElse("Unknown Class");

        // Create a new Pending Payment
        Payment payment = Payment.builder()
                .student(student)
                .classId(classId)
                .className(className) // Set the class name
                .yearMonth(yearMonth)
                .amount(amount)
                .currency("usd")
                .stripePaymentIntentId("pi_" + UUID.randomUUID().toString()) // Mock ID for now
                .status(PaymentStatus.PENDING)
                .paymentDate(LocalDateTime.now())
                .build();

        return paymentRepository.save(payment);
    }

    @Override
    public Payment completePayment(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        if (payment.getStatus() == PaymentStatus.COMPLETED) {
            return payment; // Already completed
        }

        // Update Payment Status
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setPaymentDate(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    @Override
    public List<Payment> getStudentPayments(User student) {
        return paymentRepository.findByStudent(student);
    }

    @Override
    public boolean hasAccess(User student, String classId, String yearMonth) {
        Optional<Payment> payment = paymentRepository.findByStudentAndClassIdAndYearMonthAndStatus(
                student, classId, yearMonth, PaymentStatus.COMPLETED);
        return payment.isPresent();
    }
}
