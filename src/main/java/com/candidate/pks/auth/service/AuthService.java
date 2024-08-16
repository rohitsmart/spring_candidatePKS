package com.candidate.pks.auth.service;

import com.candidate.pks.auth.dto.LoginRequest;
import com.candidate.pks.auth.dto.LoginResponse;
import com.candidate.pks.auth.model.User;
import com.candidate.pks.auth.repository.UserRepository;
import com.candidate.pks.security.AppProperties;
import com.candidate.pks.security.JwtService;
import com.candidate.pks.util.LoggerUtil;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {
    private static final Logger logger = LoggerUtil.getLogger(AuthService.class);
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final AppProperties appProperties;
    private final PasswordEncoder passwordEncoder;

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
}
