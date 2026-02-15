package com.Learn.ELP_backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.Learn.ELP_backend.model.Documents;
import java.util.List;

@Repository
public interface DocumentRepository extends MongoRepository<Documents, String> {
    List<Documents> findByClassId(String classId);

    List<Documents> findByUploadedBy(String uploadedBy);
}
