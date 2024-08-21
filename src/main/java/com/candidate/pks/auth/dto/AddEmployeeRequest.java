package com.candidate.pks.auth.dto;

import com.candidate.pks.auth.model.Designation;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AddEmployeeRequest {
    private UserRequest userRequest;
    private String empId;
    private String firstName;
    private String lastName;
    private Designation designation;
    private String joiningDate;
}
