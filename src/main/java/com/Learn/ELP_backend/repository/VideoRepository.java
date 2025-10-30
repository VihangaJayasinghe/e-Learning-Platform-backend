package com.Learn.ELP_backend.repository;

import com.Learn.ELP_backend.model.Video;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface VideoRepository extends MongoRepository<Video, String> {
    List<Video> findByCourseId(String courseId);
}