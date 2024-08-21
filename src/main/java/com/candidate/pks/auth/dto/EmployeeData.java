package com.candidate.pks.auth.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class EmployeeData {
    private String empId;
    private String fullName;//concat first and last name;
}
