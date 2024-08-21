package com.candidate.pks.auth.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserEmployeeResponseList {
    private boolean data;
    private List<EmployeeData> employeeData;
}
