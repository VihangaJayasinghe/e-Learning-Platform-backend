package com.Learn.ELP_backend.service;

import com.Learn.ELP_backend.model.Video;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface VideoService {
    Video uploadVideo(MultipartFile file, String name, String description, String courseId);
    List<Video> getAllVideos();
    Video getVideoById(String id);
    List<Video> getVideosByCourse(String courseId);
    void deleteVideo(String id);
    Video uploadTestVideo(Video video); // For testing without file upload
}