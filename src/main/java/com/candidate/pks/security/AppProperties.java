package com.candidate.pks.security;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class AppProperties {

    @Value("${app.secretKey}")
    private String secretKey;

    @Value("${app.sessionDurationMinutes}")
    private long sessionDurationMinutes;

    @Value("${app.OTP_ExpireDurationInMinutes}")
    private long otpExpireDuration;

    @Value("${app.sessionDurationDays}")
    private int sessionDurationDays;


    @Value("${jwt.SECRET_KEY}")
    private String jwtSecretKeys;


    @Value("${jwt.tokenExpirationDays}")
    private int tokenExpirationDays;


    @Value("${jwt.verifyLinkInMinutes}")
    private int verifyLinkExpire;

    @Value("${jwt.refreshTokenExpirationDays}")
    private int refreshTokenExpirationDays;

    @Value("${spring.mail.username}")
    private String mailUserName;

}