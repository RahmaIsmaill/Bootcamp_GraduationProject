package com.example.userservice.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendOtp(String email, String otp) throws MessagingException {
        MimeMessage mailMessage =mailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(mailMessage,true,"UTF-8");

        helper.setTo(email);
        helper.setFrom("rahmaismail1020@gmail.com");
        mailMessage.setSubject("Your OTP Code");
        String htmlContent = """
                    <div style="font-family: Arial, sans-serif;">
                        <h2>Account Verification</h2>
                        <p>Your OTP code is:</p>
                        <h1 style="color: #2e86de;">%s</h1>
                        <p>This code will expire in 2 Minutes.</p>
                    </div>
                    """.formatted(otp);
        helper.setText(htmlContent,true);

        mailSender.send(mailMessage);
    }
}
