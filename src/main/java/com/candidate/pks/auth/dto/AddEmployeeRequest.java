package com.candidate.pks.auth.dto;

import com.candidate.pks.auth.model.Designation;
import lombok.Data;

@Data
public class AddEmployeeRequest {
    private UserRequest userRequest;
    private String empId;
    private String firstName;
    private String lastName;
    private Designation designation;

}
