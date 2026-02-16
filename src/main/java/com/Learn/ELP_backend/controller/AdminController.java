package com.Learn.ELP_backend.controller;

import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Learn.ELP_backend.model.User;
import com.Learn.ELP_backend.model.Video;
import com.Learn.ELP_backend.service.AdminSecrity;
import com.Learn.ELP_backend.service.AdminService;
import com.Learn.ELP_backend.service.VideoService;
@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/admins")

public class AdminController {


    @Autowired
    private AdminService adminService;
    @Autowired
    private AdminSecrity adminSecrity;
    @Autowired
    private VideoService videoService;

 @GetMapping("/users")
    public ResponseEntity<?> getAll() {
        try {
            List<User> users = adminService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .body("Error retrieving users: " + e.getMessage());
        }
    }
   
@GetMapping("/users/{username}")
public ResponseEntity<?> getuserbyname(@PathVariable String username) {
    try {
        User user = adminService.getuserByName(username);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND)
                    .body("User not found with username: " + username);
        }
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .body("Error retrieving user: " + e.getMessage());
    }
}



@GetMapping("/users/role/{role}")
    public ResponseEntity<?> getByRole(@PathVariable String role) {
        try {
            List<User> users = adminService.getUserByRole(role);
            if (users != null && !users.isEmpty()) {
                return ResponseEntity.ok(users);
            } else {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND)
                        .body("No users found with role: " + role);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .body("Error retrieving users by role: " + e.getMessage());
        }
    }


@PutMapping("/users/{username}/{key}")
public ResponseEntity<?> updateUser(
        @PathVariable String username,
        @PathVariable String key,
        @RequestBody User request) {
    try {
        // Validate security key first
        adminSecrity.validateKey(key);
        
        // If key is valid, proceed with update
        User updatedUser = adminService.updateUser(username, request);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND)
                    .body("User not found with username: " + username);
        }
    } catch (SecurityException e) {
        return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED)
                .body("Invalid security key: " + e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .body("Error updating user: " + e.getMessage());
    }
}


    @DeleteMapping("/users/{username}/{key}")
public ResponseEntity<?> delete(
        @PathVariable String username, 
        @PathVariable String key) {
    try {
        // Validate security key first
        adminSecrity.validateKey(key);
        
        // If key is valid, proceed with deletion
        adminService.deleteUser(username);
        return ResponseEntity.ok("User '" + username + "' deleted successfully");
    } catch (SecurityException e) {
        return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED)
                .body("Invalid security key: " + e.getMessage());
    } catch (RuntimeException e) {
        if (e.getMessage().contains("not found")) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND)
                    .body("User not found with username: " + username);
        }
        return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .body("Error deleting user: " + e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .body("Error deleting user: " + e.getMessage());
    }
}


 @GetMapping("/videos")
    public ResponseEntity<?> getAllVideos() {
        try {
            List<Video> videos = videoService.getAllVideos();
            return ResponseEntity.ok(videos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .body("Error retrieving videos: " + e.getMessage());
        }
    }

@DeleteMapping("/videos/{id}/{key}")
    public ResponseEntity<?> deleteVideo(@PathVariable String id, @PathVariable String key) {
        try {
            adminSecrity.validateKey(key);
            videoService.deleteVideo(id);
            return ResponseEntity.ok("Video with ID '" + id + "' deleted successfully");
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED)
                    .body("Invalid security key: " + e.getMessage());
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND)
                        .body("Video not found with ID: " + id);
            }
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .body("Error deleting video: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .body("Error deleting video: " + e.getMessage());
        }
    }


    @GetMapping("/videos/uploadedBy/{username}")
    public ResponseEntity<?> getVideosByUploader(@PathVariable String username) {
        try {
            List<Video> videos = videoService.getVideosByUploadedBy(username);
            if (videos != null && !videos.isEmpty()) {
                return ResponseEntity.ok(videos);
            } else {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND)
                        .body("No videos found uploaded by user: " + username);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .body("Error retrieving videos: " + e.getMessage());
        }
    }

    // System Stats
    @GetMapping("/stats")
    public ResponseEntity<?> getSystemStats() {
        try {
            com.Learn.ELP_backend.dto.SystemStatsDTO stats = adminService.getSystemStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .body("Error retrieving system stats: " + e.getMessage());
        }
    }

    // Course Management
    @GetMapping("/courses")
    public ResponseEntity<?> getAllCourses() {
        try {
            List<com.Learn.ELP_backend.model.Course> courses = adminService.getAllCourses();
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .body("Error retrieving courses: " + e.getMessage());
        }
    }

    @GetMapping("/courses/instructor/{username}")
    public ResponseEntity<?> getCoursesByInstructor(@PathVariable String username) {
        try {
            List<com.Learn.ELP_backend.model.Course> courses = adminService.getCoursesByInstructor(username);
            if (courses != null && !courses.isEmpty()) {
                return ResponseEntity.ok(courses);
            } else {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND)
                        .body("No courses found for instructor: " + username);
            }
        } catch (RuntimeException e) {
             if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND)
                        .body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .body("Error retrieving courses: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .body("Error retrieving courses: " + e.getMessage());
        }
    }

    @PutMapping("/courses/{id}/publish/{key}")
    public ResponseEntity<?> publishCourse(@PathVariable String id, @PathVariable String key) {
        try {
            adminSecrity.validateKey(key);
            com.Learn.ELP_backend.model.Course course = adminService.publishCourse(id);
            return ResponseEntity.ok(course);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED)
                    .body("Invalid security key: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .body("Error publishing course: " + e.getMessage());
        }
    }

    @DeleteMapping("/courses/{id}/{key}")
    public ResponseEntity<?> deleteCourse(@PathVariable String id, @PathVariable String key) {
        try {
            adminSecrity.validateKey(key);
            adminService.deleteCourse(id);
            return ResponseEntity.ok("Course with ID '" + id + "' deleted successfully");
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED)
                    .body("Invalid security key: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .body("Error deleting course: " + e.getMessage());
        }
    }

    // Class Management
    @GetMapping("/classes")
    public ResponseEntity<?> getAllClasses() {
        try {
            List<com.Learn.ELP_backend.model.Class> classes = adminService.getAllClasses();
            return ResponseEntity.ok(classes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .body("Error retrieving classes: " + e.getMessage());
        }
    }

    @DeleteMapping("/classes/{id}/{key}")
    public ResponseEntity<?> deleteClass(@PathVariable String id, @PathVariable String key) {
        try {
            adminSecrity.validateKey(key);
            adminService.deleteClass(id);
            return ResponseEntity.ok("Class with ID '" + id + "' deleted successfully");
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED)
                    .body("Invalid security key: " + e.getMessage());
        } catch (RuntimeException e) {
             if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND)
                        .body("Class not found with ID: " + id);
            }
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .body("Error deleting class: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .body("Error deleting class: " + e.getMessage());
        }
    }

}
