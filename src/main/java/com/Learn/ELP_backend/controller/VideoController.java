package com.Learn.ELP_backend.controller;

import com.Learn.ELP_backend.model.Video;
import com.Learn.ELP_backend.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    @Autowired
    private VideoService videoService;

    // Upload video
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    @PostMapping("/upload")
    public ResponseEntity<Video> uploadVideo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("description") String description) {
        
        Video video = videoService.uploadVideo(file, name, description);
        return ResponseEntity.ok(video);
    }

    // Create test video (without file upload)
    
    @PostMapping("/test")
    public ResponseEntity<Video> createTestVideo() {
        Video testVideo = Video.builder()
                .videoName("Test Video")
                .description("This is a test video")
                .firebaseUrl("https://firebasestorage.googleapis.com/v0/b/your-bucket/videos/test.mp4")
                .fileName("test.mp4")
                .fileSize(1048576L)
                .contentType("video/mp4")
                .uploadedBy("test-user")
                .uploadDate(LocalDateTime.now())
                .build();
        
        Video savedVideo = videoService.uploadTestVideo(testVideo);
        return ResponseEntity.ok(savedVideo);
    }

    // Get all videos
    @GetMapping
    public ResponseEntity<List<Video>> getAllVideos() {
        List<Video> videos = videoService.getAllVideos();
        return ResponseEntity.ok(videos);
    }

    // Get video by ID
    @GetMapping("/{id}")
    public ResponseEntity<Video> getVideoById(@PathVariable String id) {
        Video video = videoService.getVideoById(id);
        if (video != null) {
            return ResponseEntity.ok(video);
        }
        return ResponseEntity.notFound().build();
    }

    // Delete video
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVideo(@PathVariable String id) {
        videoService.deleteVideo(id);
        return ResponseEntity.ok().build();
    }
}