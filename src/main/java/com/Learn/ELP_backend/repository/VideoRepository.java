package com.Learn.ELP_backend.repository;

import com.Learn.ELP_backend.model.Video;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface VideoRepository extends MongoRepository<Video, String> {

    List<Video> findByUploadedBy(String uploadedBy);
}