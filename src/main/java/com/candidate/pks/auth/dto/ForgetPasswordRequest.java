package com.candidate.pks.auth.dto;

import lombok.Data;

@Data
public class ForgetPasswordRequest {
    private String email;
    private String otp;
    private String newPassword;
}
