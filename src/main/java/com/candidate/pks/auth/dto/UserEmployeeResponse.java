package com.candidate.pks.auth.dto;

import com.candidate.pks.auth.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEmployeeResponse {
    private String username;
    private String firstName;
    private String lastName;
    private String empId;
    private String designation;
    private UserRole role;
}
