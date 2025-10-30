package com.Learn.ELP_backend.service.impl;

import com.Learn.ELP_backend.model.Video;
import com.Learn.ELP_backend.repository.VideoRepository;
import com.Learn.ELP_backend.service.CloudinaryStorageService;
import com.Learn.ELP_backend.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoRepository videoRepository;
    
    @Autowired
    private CloudinaryStorageService cloudinaryStorageService;

    @Override
    public Video uploadVideo(MultipartFile file, String name, String description, String courseId) {
        try {
            // Validate file
            if (file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }
            
            // 1. Upload to Cloudinary and get REAL URL
            String cloudinaryUrl = cloudinaryStorageService.uploadVideo(file);
            
            // 2. Save metadata to MongoDB
            Video video = Video.builder()
                    .videoName(name)
                    .description(description)
                    .fileName(file.getOriginalFilename())
                    .fileSize(file.getSize())
                    .contentType(file.getContentType())
                    .firebaseUrl(cloudinaryUrl) // Now Cloudinary URL
                    .courseId(courseId)
                    .uploadedBy("user123")
                    .uploadDate(LocalDateTime.now())
                    .build();
            
            return videoRepository.save(video);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload video: " + e.getMessage());
        }
    }

    // ... keep all other methods the same
    @Override
    public Video uploadTestVideo(Video video) {
        return videoRepository.save(video);
    }

    @Override
    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }

    @Override
    public Video getVideoById(String id) {
        return videoRepository.findById(id).orElse(null);
    }

    @Override
    public List<Video> getVideosByCourse(String courseId) {
        return videoRepository.findByCourseId(courseId);
    }

    @Override
    public void deleteVideo(String id) {
        Video video = videoRepository.findById(id).orElse(null);
        if (video != null) {
            // Delete from Cloudinary
            cloudinaryStorageService.deleteVideo(video.getFirebaseUrl());
            // Delete from MongoDB
            videoRepository.deleteById(id);
        }
    }
}