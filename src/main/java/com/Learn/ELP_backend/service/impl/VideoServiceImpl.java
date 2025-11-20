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
    public Video uploadVideo(MultipartFile file, String name, String description) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }
            
            String cloudinaryUrl = cloudinaryStorageService.uploadVideo(file);
            
            Video video = Video.builder()
                    .videoName(name)
                    .description(description)
                    .fileName(file.getOriginalFilename())
                    .fileSize(file.getSize())
                    .contentType(file.getContentType())
                    .firebaseUrl(cloudinaryUrl)
                    .uploadedBy("user123")
                    .uploadDate(LocalDateTime.now())
                    .build();
            
            return videoRepository.save(video);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload video: " + e.getMessage());
        }
    }

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
    public void deleteVideo(String id) {
        Video video = videoRepository.findById(id).orElse(null);
        if (video != null) {
            try {
                // Delete from Cloudinary first
                cloudinaryStorageService.deleteVideo(video.getFirebaseUrl());
                // Only delete from MongoDB if Cloudinary succeeds
                videoRepository.deleteById(id);
            } catch (Exception e) {
                throw new RuntimeException("Failed to delete video from storage: " + e.getMessage());
            }
        }
    }

    @Override
    public List<Video> getVideosByUploadedBy(String uploadedBy) {
   try {
            List<Video> videos = videoRepository.findByUploadedBy(uploadedBy);
            return videos;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving videos by uploader: " + e.getMessage());
        }
}
}