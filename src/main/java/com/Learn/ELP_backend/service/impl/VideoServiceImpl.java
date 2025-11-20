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
            System.out.println("Starting video upload: " + name);
            
            if (file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }
            
            // Upload to Cloudinary using the simple method
            CloudinaryStorageService.VideoUploadResult uploadResult = 
                cloudinaryStorageService.uploadVideoWithABS(file);
            
            System.out.println("Cloudinary upload successful!");
            System.out.println("Original MP4 URL: " + uploadResult.getSecureUrl());
            System.out.println("HLS URL: " + uploadResult.getPlaybackUrl());
            
            // Create video entity
            Video video = Video.builder()
                    .videoName(name)
                    .description(description)
                    .fileName(file.getOriginalFilename())
                    .fileSize(file.getSize())
                    .contentType(file.getContentType())
                    .firebaseUrl(uploadResult.getSecureUrl()) // Original MP4 URL
                    .playbackUrl(uploadResult.getPlaybackUrl()) // HLS URL with sp_auto
                    .status(Video.VideoStatus.READY)
                    .processingJobId(uploadResult.getJobId())
                    .uploadedBy("user123")
                    .uploadDate(LocalDateTime.now())
                    .build();
            
            Video savedVideo = videoRepository.save(video);
            System.out.println("Video saved to database with ID: " + savedVideo.getId());
            System.out.println("Video ready for adaptive streaming with multiple qualities");
            
            return savedVideo;
            
        } catch (Exception e) {
            System.err.println("Video upload failed: " + e.getMessage());
            e.printStackTrace();
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
    public List<Video> getReadyVideos() {
        return videoRepository.findByStatus(Video.VideoStatus.READY);
    }
    
    @Override
    public Video checkProcessingStatus(String videoId) {
        Video video = getVideoById(videoId);
        return video;
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
                System.out.println("Video deleted successfully: " + id);
            } catch (Exception e) {
                throw new RuntimeException("Failed to delete video from storage: " + e.getMessage());
            }
        }
    }
}