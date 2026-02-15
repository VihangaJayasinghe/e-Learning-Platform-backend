package com.Learn.ELP_backend.repository;

import com.Learn.ELP_backend.model.Payment;
import com.Learn.ELP_backend.model.PaymentStatus;
import com.Learn.ELP_backend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {

    List<Payment> findByStudent(User student);

    List<Payment> findByClassId(String classId);

    Optional<Payment> findByStripePaymentIntentId(String stripePaymentIntentId);

    // For checking access: Has the student paid for this specific month?
    Optional<Payment> findByStudentAndClassIdAndYearMonthAndStatus(User student, String classId, String yearMonth,
            PaymentStatus status);

    // For Teacher Analytics: Find completed payments for a list of classes
    List<Payment> findByClassIdInAndStatus(List<String> classIds, PaymentStatus status);
}
