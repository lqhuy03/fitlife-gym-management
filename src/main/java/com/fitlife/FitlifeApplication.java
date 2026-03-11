package com.fitlife;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync // Sửa từ @EnabledAsync thành @EnableAsync là hết đỏ ngay em nhé
public class FitlifeApplication {
    public static void main(String[] args) {
        SpringApplication.run(FitlifeApplication.class, args);
    }
}