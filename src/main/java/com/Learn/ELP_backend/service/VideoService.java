package com.Learn.ELP_backend.service;

import com.Learn.ELP_backend.model.Video;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface VideoService {
    Video uploadVideo(MultipartFile file, String name, String description);
    List<Video> getAllVideos();
    Video getVideoById(String id);
    void deleteVideo(String id);
    Video uploadTestVideo(Video video); // For testing without file upload
    List<Video> getVideosByUploadedBy(String uploadedBy);
}