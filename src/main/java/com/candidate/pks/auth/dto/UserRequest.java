package com.candidate.pks.auth.dto;

import com.candidate.pks.auth.model.UserRole;
import lombok.Data;

@Data
public class UserRequest {
    private String email;
    private UserRole userRole;
}
