package com.candidate.pks.service;

import com.candidate.pks.security.AppProperties;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;

@Service
@Slf4j
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendSimpleMessage(String to, String subject, String text) {
        log.info("Sending simple message to: {}", to);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(appProperties.getMailUserName());
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
        log.info("Simple message sent to: {}", to);
    }

    public void sendUserPassword(String to, String firstName, String lastName, String empId, String password) throws MessagingException, IOException {
        log.info("Preparing to send email to: {}", to);

        Context context = new Context();
        context.setVariable("firstName", firstName);
        context.setVariable("lastName", lastName);
        context.setVariable("empId", empId);
        context.setVariable("password", password);

        String emailContent = templateEngine.process("sendPassword-template", context);
        log.debug("Email content prepared: {}", emailContent);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        helper.setTo(to);
        helper.setSubject("Welcome to PerfectKode");
        helper.setText(emailContent, true);

        javaMailSender.send(message);
        log.info("Email sent to: {}", to);
    }

    public void sendWelcomeEmail(String to, String firstName, String lastName) throws MessagingException, IOException {
        log.info("Preparing to send welcome email to: {}", to);

        Context context = new Context();
        context.setVariable("firstName", firstName);
        context.setVariable("lastName", lastName);

        String emailContent = templateEngine.process("welcome-candidate-template", context);
        log.debug("Email content prepared: {}", emailContent);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        helper.setTo(to);
        helper.setSubject("Welcome to PerfectKode");
        helper.setText(emailContent, true);

        javaMailSender.send(message);
        log.info("Welcome email sent to: {}", to);
    }

    public void sendInterviewNotificationToCandidate(String to, String firstName, String interviewDate, String interviewerName) throws MessagingException, IOException {
        log.info("Preparing to send interview notification to candidate: {}", to);

        Context context = new Context();
        context.setVariable("firstName", firstName);
        context.setVariable("interviewDate", interviewDate);
        context.setVariable("interviewerName", interviewerName);

        String emailContent = templateEngine.process("candidate-interview-template", context);
        log.debug("Email content prepared: {}", emailContent);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        helper.setTo(to);
        helper.setSubject("Interview Scheduled - PerfectKode");
        helper.setText(emailContent, true);

        javaMailSender.send(message);
        log.info("Interview notification sent to candidate: {}", to);
    }

    public void sendInterviewNotificationToEmployee(String to, String firstName, String interviewDate, String candidateName) throws MessagingException, IOException {
        log.info("Preparing to send interview notification to employee: {}", to);

        Context context = new Context();
        context.setVariable("firstName", firstName);
        context.setVariable("interviewDate", interviewDate);
        context.setVariable("candidateName", candidateName);

        String emailContent = templateEngine.process("employee-interview-template", context);
        log.debug("Email content prepared: {}", emailContent);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        helper.setTo(to);
        helper.setSubject("Interview Scheduled - PerfectKode");
        helper.setText(emailContent, true);

        javaMailSender.send(message);
        log.info("Interview notification sent to employee: {}", to);
    }

    public void sendOtpToEmployee(String to, String firstName, String lastName, String otp) throws MessagingException, IOException {
        log.info("Preparing to send OTP to employee: {}", to);

        Context context = new Context();
        context.setVariable("firstName", firstName);
        context.setVariable("lastName", lastName);
        context.setVariable("otp", otp);

        String emailContent = templateEngine.process("otp-email-template", context);
        log.debug("Email content prepared: {}", emailContent);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        helper.setTo(to);
        helper.setSubject("Your OTP Code - PerfectKode");
        helper.setText(emailContent, true);

        javaMailSender.send(message);
        log.info("OTP sent to employee: {}", to);
    }


}
