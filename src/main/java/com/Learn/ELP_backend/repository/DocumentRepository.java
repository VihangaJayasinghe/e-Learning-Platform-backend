package com.Learn.ELP_backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.Learn.ELP_backend.model.Documents;
import java.util.List;

public interface DocumentRepository extends MongoRepository<Documents, String>{
    List<Documents> findByClassId(String classId);
}
