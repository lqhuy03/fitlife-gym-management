package com.fitlife.service;

import org.springframework.scheduling.annotation.Async;

public interface EmailService {
    @Async
    void sendWelcomeEmail(String toEmail, String fullName);

    @Async
    void sendPasswordResetEmail(String toEmail, String otp);
}
