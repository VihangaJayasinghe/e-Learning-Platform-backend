package com.Learn.ELP_backend.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;

@Configuration
public class EnvConfig {

    @PostConstruct
    public void loadEnv() {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        setSystemProperty("MONGODB_URI", dotenv);
        setSystemProperty("GOOGLE_CLIENT_ID", dotenv);
        setSystemProperty("GOOGLE_CLIENT_SECRET", dotenv);
        setSystemProperty("EMAIL_USERNAME", dotenv);
        setSystemProperty("EMAIL_PASSWORD", dotenv);
        setSystemProperty("JWT_SECRET", dotenv);
        setSystemProperty("CLOUDINARY_CLOUD_NAME", dotenv);
        setSystemProperty("CLOUDINARY_API_KEY", dotenv);
        setSystemProperty("CLOUDINARY_API_SECRET", dotenv);
        setSystemProperty("STRIPE_SECRET_KEY", dotenv);

        System.out.println("Environment variables configured.");
    }

    private void setSystemProperty(String key, Dotenv dotenv) {
        String value = System.getenv(key);
        if (value == null || value.isEmpty()) {
            value = dotenv.get(key);
        }
        if (value != null) {
            System.setProperty(key, value);
        }
    }
}