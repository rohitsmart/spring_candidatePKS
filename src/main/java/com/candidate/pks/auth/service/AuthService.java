package com.candidate.pks.auth.service;

import com.candidate.pks.auth.dto.ForgetPasswordRequest;
import com.candidate.pks.auth.dto.LoginRequest;
import com.candidate.pks.auth.dto.LoginResponse;
import com.candidate.pks.auth.model.User;
import com.candidate.pks.auth.repository.UserRepository;
import com.candidate.pks.exception.UsernameNotFoundException;
import com.candidate.pks.repeat.Response;
import com.candidate.pks.security.AppProperties;
import com.candidate.pks.security.JwtService;
import com.candidate.pks.service.MailService;
import com.candidate.pks.util.LoggerUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.Context;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private static final Logger logger = LoggerUtil.getLogger(AuthService.class);
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final AppProperties appProperties;
    private final PasswordEncoder passwordEncoder;
    private  final MailService mailService;

    public LoginResponse login(LoginRequest request) {
        try {
            String username = request.getEmail();
            String password = request.getPassword();
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    password
            );
            authentication = authenticationManager.authenticate(authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = (User) authentication.getPrincipal();
            String bearerToken = jwtService.generateToken(user);
            logger.info("User {} logged in successfully", username);
            return LoginResponse.builder()
                    .token(bearerToken)
                    .build();
        } catch (BadCredentialsException e) {
            logger.error("Invalid username or password", e);
            throw new BadCredentialsException("Invalid username or password.");
        } catch (Exception e) {
            logger.error("Error caught inside login: {}", e.getLocalizedMessage(), e);
            throw new RuntimeException("Error caught inside login: " + e.getLocalizedMessage());
        }
    }

    public Response forgetPassword(ForgetPasswordRequest forgetPasswordRequest) {
        User user = userRepository.findByUsername(forgetPasswordRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Email does not exist!"));

        String otp = generateOtp();
//        user.setOtp(otp);
//        user.setOtpExpiration(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10));
//        userRepository.save(user);

        // Send OTP email
        try {
            log.info("Sending email to: {}", forgetPasswordRequest.getEmail());
            mailService.sendOtpToEmployee(user.getUsername(),user.getEmployee().getFirstName(),user.getEmployee().getLastName(),otp);
        } catch (MessagingException | IOException e) {
            log.error("Error sending email to {}: {}", forgetPasswordRequest.getEmail(), e.getMessage());
            return new Response("Employee created, but email sending failed.");
        }

        return new Response("OTP sent to your email.");
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 1000 + random.nextInt(9000);
        return String.valueOf(otp);
    }

    public Response resetPassword(ForgetPasswordRequest forgetPasswordRequest) {
        try{
            User user = userRepository.findByUsername(forgetPasswordRequest.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("Email does not exist!"));

            // validate otp

            user.setPassword(new User().getHashPassword(forgetPasswordRequest.getNewPassword()));
            return Response.builder()
                    .message("Password Reset Successfully")
                    .build();

        }catch (Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }
}
