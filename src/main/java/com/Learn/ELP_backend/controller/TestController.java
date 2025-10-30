package com.Learn.ELP_backend.controller;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    
    @GetMapping("/test")
    public String test() {
        return "Backend is working! 🎉";
    }


@GetMapping("/test-env")
public String testEnv() {
    try {
        Dotenv dotenv = Dotenv.configure().directory(".").load();
        String cloudName = dotenv.get("CLOUDINARY_CLOUD_NAME");
        String apiKey = dotenv.get("CLOUDINARY_API_KEY");
        String apiSecret = dotenv.get("CLOUDINARY_API_SECRET");
        
        return "Cloudinary Config Test:<br>" +
               "Cloud Name: " + (cloudName != null ? "✓" : "✗") + "<br>" +
               "API Key: " + (apiKey != null ? "✓" : "✗") + "<br>" +
               "API Secret: " + (apiSecret != null ? "✓" : "✗");
    } catch (Exception e) {
        return "Error: " + e.getMessage();
    }
}
}