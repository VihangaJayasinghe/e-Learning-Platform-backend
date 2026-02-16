package com.Learn.ELP_backend.repository;

import com.Learn.ELP_backend.model.Enrollment;
import com.Learn.ELP_backend.model.User;
import com.Learn.ELP_backend.model.Class;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends MongoRepository<Enrollment, String> {
    Optional<Enrollment> findByStudentAndClassEnrolled(User student, Class classEnrolled);

    List<Enrollment> findByStudent(User student);

    List<Enrollment> findByClassEnrolled(Class classEnrolled);

    List<Enrollment> findByClassEnrolledIn(List<Class> classes);
}
