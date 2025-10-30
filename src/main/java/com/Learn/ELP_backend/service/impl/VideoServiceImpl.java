package com.Learn.ELP_backend.service.impl;

import com.Learn.ELP_backend.model.Video;
import com.Learn.ELP_backend.repository.VideoRepository;
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

    @Override
    public Video uploadVideo(MultipartFile file, String name, String description, String courseId) {
        // Simulate Firebase upload for now
        String simulatedFirebaseUrl = "https://example.com/videos/" + file.getOriginalFilename();
        
        Video video = Video.builder()
                .videoName(name)
                .description(description)
                .fileName(file.getOriginalFilename())
                .fileSize(file.getSize())
                .contentType(file.getContentType())
                .firebaseUrl(simulatedFirebaseUrl)
                .courseId(courseId)
                .uploadedBy("user123")
                .uploadDate(LocalDateTime.now())
                .build();
        
        return videoRepository.save(video);
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
    public List<Video> getVideosByCourse(String courseId) {
        return videoRepository.findByCourseId(courseId);
    }

    @Override
    public void deleteVideo(String id) {
        videoRepository.deleteById(id);
    }
}