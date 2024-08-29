package com.candidate.pks.auth.dto;

import lombok.Data;

@Data
public class AddEmployeeRequest {
    private UserRequest userRequest;
    private String empId;
    private String firstName;
    private String lastName;
    private String designation;
    private String joiningDate;
}
