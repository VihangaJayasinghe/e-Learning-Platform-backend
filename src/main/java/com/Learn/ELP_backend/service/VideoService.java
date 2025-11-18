package com.Learn.ELP_backend.service;

import com.Learn.ELP_backend.model.Video;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoService {
    Video uploadVideo(MultipartFile file, String name, String description);
    Video uploadTestVideo(Video video);
    List<Video> getAllVideos();
    Video getVideoById(String id);
    List<Video> getReadyVideos();
    Video checkProcessingStatus(String videoId);
    void deleteVideo(String id);
}