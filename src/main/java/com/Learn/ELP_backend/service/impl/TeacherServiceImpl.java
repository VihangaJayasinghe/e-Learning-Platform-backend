package com.Learn.ELP_backend.service.impl;

import com.Learn.ELP_backend.dto.RecentEnrollmentDTO;
import com.Learn.ELP_backend.dto.TeacherAnalyticsDTO;
import com.Learn.ELP_backend.model.Class;
import com.Learn.ELP_backend.model.Enrollment;
import com.Learn.ELP_backend.model.Payment;
import com.Learn.ELP_backend.model.PaymentStatus;
import com.Learn.ELP_backend.model.User;
import com.Learn.ELP_backend.repository.ClassRepository;
import com.Learn.ELP_backend.repository.EnrollmentRepository;
import com.Learn.ELP_backend.repository.PaymentRepository;
import com.Learn.ELP_backend.repository.UserRepository;
import com.Learn.ELP_backend.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public TeacherAnalyticsDTO getAnalytics(String username) {
        // 1. Find Teacher
        User teacher = userRepository.findByUsername(username);
        if (teacher == null) {
            throw new RuntimeException("Teacher not found");
        }

        // 2. Fetch all classes for the teacher
        List<Class> classes = classRepository.findByTeacher(teacher);

        if (classes.isEmpty()) {
            return new TeacherAnalyticsDTO(0.0, 0L, 0L, 0.0, List.of());
        }

        List<String> classIds = classes.stream().map(Class::getId).collect(Collectors.toList());

        // 3. Calculate Earnings
        List<Payment> payments = paymentRepository.findByClassIdInAndStatus(classIds, PaymentStatus.COMPLETED);
        Double totalEarnings = payments.stream()
                .filter(p -> p.getAmount() != null)
                .mapToDouble(Payment::getAmount)
                .sum();

        // 4. Calculate Unique Students
        List<Enrollment> enrollments = enrollmentRepository.findByClassEnrolledIn(classes);
        Long totalStudents = enrollments.stream()
                .filter(e -> e.getStudent() != null)
                .map(e -> e.getStudent().getId())
                .distinct()
                .count();

        // 5. Total Courses
        Long totalCourses = (long) classes.size();

        // 6. Average Rating
        Double averageRating = classes.stream()
                .mapToDouble(c -> c.getAverageRating() != null ? c.getAverageRating() : 0.0)
                .average()
                .orElse(0.0);

        // 7. Recent Enrollments
        List<RecentEnrollmentDTO> recentEnrollments = enrollments.stream()
                .filter(e -> e.getEnrollmentDate() != null)
                .sorted(Comparator.comparing(Enrollment::getEnrollmentDate).reversed())
                .limit(5)
                .map(e -> new RecentEnrollmentDTO(
                        e.getStudent() != null ? e.getStudent().getUsername() : "Unknown",
                        e.getClassEnrolled() != null ? e.getClassEnrolled().getClassName() : "Unknown",
                        e.getEnrollmentDate()))
                .collect(Collectors.toList());

        return new TeacherAnalyticsDTO(totalEarnings, totalStudents, totalCourses, averageRating, recentEnrollments);
    }
}
