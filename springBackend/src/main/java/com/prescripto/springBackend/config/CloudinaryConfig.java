package com.prescripto.springBackend.config;

import com.cloudinary.Cloudinary;
import com.prescripto.springBackend.util.EnvUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", EnvUtil.getCloudName());
        config.put("api_key", EnvUtil.getCloudApiKey());
        config.put("api_secret", EnvUtil.getCloudSecretKey());
        return new Cloudinary(config);
    }
}

