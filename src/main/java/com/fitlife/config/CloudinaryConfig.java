package com.fitlife.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dvseaoy5k",
                "api_key", "637865418153447",
                "api_secret", "9rFPfkEBMFnYtqJdu0m59l8MVpM",
                "secure", true
        ));
    }
}