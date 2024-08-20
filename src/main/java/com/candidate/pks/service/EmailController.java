package com.candidate.pks.service;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/public/email/")
@RequiredArgsConstructor
@Tag(name = "Email Management", description = "Endpoints for Email Service Management.")
public class EmailController {

    @Autowired
    private MailService mailService;

    @PostMapping("/send-verification")
    public String sendVerificationEmail(@RequestParam String email) {
        String subject = "Email Verification";
        String text = "Please click on the link to verify your email: [verification link]";
        mailService.sendSimpleMessage(email, subject, text);
        return "Verification email sent.";
    }

    @PostMapping("/send-password-reset")
    public String sendPasswordResetEmail(@RequestParam String email) {
        String subject = "Password Reset Request";
        String text = "Please click on the link to reset your password: [reset link]";
        mailService.sendSimpleMessage(email, subject, text);
        return "Password reset email sent.";
    }
}

