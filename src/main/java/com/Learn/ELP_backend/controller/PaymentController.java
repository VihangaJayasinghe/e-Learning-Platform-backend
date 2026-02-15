package com.Learn.ELP_backend.controller;

import com.Learn.ELP_backend.model.Payment;
import com.Learn.ELP_backend.model.User;
import com.Learn.ELP_backend.model.Class;
import com.Learn.ELP_backend.service.PaymentService;
import com.Learn.ELP_backend.repository.UserRepository;
import com.Learn.ELP_backend.repository.ClassRepository;
import com.Learn.ELP_backend.repository.PaymentRepository;
import com.Learn.ELP_backend.model.PaymentStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    // STUDENT ENDPOINTS

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/payments/initiate")
    public ResponseEntity<?> initiatePayment(@RequestBody Map<String, Object> payload, Principal principal) {
        String username = principal.getName();
        User student = userRepository.findByUsername(username);

        String classId = (String) payload.get("classId");
        String yearMonth = (String) payload.get("yearMonth");
        Double amount = Double.valueOf(payload.get("amount").toString());

        Payment payment = paymentService.initiatePayment(student, classId, yearMonth, amount);
        return ResponseEntity.ok(payment);
    }

    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @PostMapping("/payments/complete/{paymentId}")
    public ResponseEntity<?> completePayment(@PathVariable String paymentId) {
        try {
            Payment payment = paymentService.completePayment(paymentId);
            return ResponseEntity.ok(payment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/payments/my-payments")
    public ResponseEntity<List<Payment>> getMyPayments(Principal principal) {
        String username = principal.getName();
        User student = userRepository.findByUsername(username);

        List<Payment> payments = paymentService.getStudentPayments(student);
        return ResponseEntity.ok(payments);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/payments/access/{classId}/{yearMonth}")
    public ResponseEntity<Boolean> checkAccess(@PathVariable String classId, @PathVariable String yearMonth,
            Principal principal) {
        String username = principal.getName();
        User student = userRepository.findByUsername(username);

        boolean hasAccess = paymentService.hasAccess(student, classId, yearMonth);
        return ResponseEntity.ok(hasAccess);
    }

    // TEACHER ENDPOINTS

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/teachers/payments")
    public ResponseEntity<?> getTeacherPayments(Principal principal) {
        String username = principal.getName();
        User teacher = userRepository.findByUsername(username);

        // 1. Find all classes taught by this teacher
        List<Class> classes = classRepository.findByTeacher(teacher);

        if (classes.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }

        List<String> classIds = classes.stream()
                .map(Class::getId)
                .collect(Collectors.toList());

        // 2. Find all COMPLETED payments for these classes
        List<Payment> payments = paymentRepository.findByClassIdInAndStatus(classIds, PaymentStatus.COMPLETED);

        // 3. Map class names for better display
        // We can do this efficiently by creating a map of classId -> className
        Map<String, String> classMap = classes.stream()
                .collect(Collectors.toMap(Class::getId, Class::getClassName));

        payments.forEach(p -> p.setClassName(classMap.get(p.getClassId())));

        return ResponseEntity.ok(payments);
    }
}
