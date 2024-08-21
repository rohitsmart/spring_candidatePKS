package com.candidate.pks.auth.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class EmployeeResponseDTO {
    private Integer empId;
    private String name;
    private String designation;
    private String email;
    private LocalDate joiningDate;

}

