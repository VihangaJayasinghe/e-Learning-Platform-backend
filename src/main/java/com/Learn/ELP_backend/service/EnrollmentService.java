package com.Learn.ELP_backend.service;

import com.Learn.ELP_backend.model.Class;
import com.Learn.ELP_backend.model.Enrollment;
import com.Learn.ELP_backend.model.EnrollmentStatus;
import com.Learn.ELP_backend.model.PaymentStatus;
import com.Learn.ELP_backend.model.User;
import com.Learn.ELP_backend.repository.ClassRepository;
import com.Learn.ELP_backend.repository.EnrollmentRepository;
import com.Learn.ELP_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private UserRepository userRepository;

    public Enrollment enrollStudent(User student, String classId) {
        Class classEnrolled = classRepository.findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("Class not found with id: " + classId));

        if (enrollmentRepository.findByStudentAndClassEnrolled(student, classEnrolled).isPresent()) {
            throw new IllegalArgumentException("Student is already enrolled in this class");
        }

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .classEnrolled(classEnrolled)
                .enrollmentDate(LocalDateTime.now())
                .status(EnrollmentStatus.APPROVED) // For now, auto-approve
                .paymentStatus(PaymentStatus.PENDING) // Default to pending
                .build();

        return enrollmentRepository.save(enrollment);
    }

    public List<Enrollment> getStudentEnrollments(User student) {
        return enrollmentRepository.findByStudent(student);
    }

    public boolean isEnrolled(String userId, String classId) {
        // Need to fetch User and Class entities to use the repository method
        // Or we could method in repository to find by IDs if we wanted to optimization
        // For now, let's fetch entities.

        Optional<User> studentOpt = userRepository.findById(userId);
        Optional<Class> classOpt = classRepository.findById(classId);

        if (studentOpt.isEmpty() || classOpt.isEmpty()) {
            return false;
        }

        return enrollmentRepository.findByStudentAndClassEnrolled(studentOpt.get(), classOpt.get()).isPresent();
    }
}
