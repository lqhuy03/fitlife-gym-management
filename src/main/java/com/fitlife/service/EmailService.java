package com.fitlife.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Async // Gửi ngầm để không làm chậm quá trình đăng ký của User
    public void sendWelcomeEmail(String toEmail, String fullName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            String htmlBody = "<html><body style='font-family: Arial, sans-serif;'>"
                    + "<div style='background-color: #f8f9fa; padding: 20px; border-radius: 10px;'>"
                    + "<h1 style='color: #007bff;'>Chào mừng " + fullName + " đến với FitLife Gym!</h1>"
                    + "<p>Chúng tôi rất vui mừng khi bạn đã lựa chọn FitLife để bắt đầu hành trình thay đổi bản thân.</p>"
                    + "<p>Tài khoản của bạn đã được khởi tạo thành công. Hãy đăng nhập ngay để khám phá các lộ trình tập luyện xịn xò nhé!</p>"
                    + "<br><a href='#' style='background-color: #28a745; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;'>Bắt đầu tập luyện ngay</a>"
                    + "<p style='font-size: 12px; color: #6c757d; margin-top: 20px;'>Đội ngũ FitLife Support.</p>"
                    + "</div></body></html>";

            helper.setTo(toEmail);
            helper.setSubject("🔥 Chào mừng thành viên mới của FitLife!");
            helper.setText(htmlBody, true);
            helper.setFrom("fitlife-system@gmail.com");

            mailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("Lỗi gửi email: " + e.getMessage());
        }
    }
}