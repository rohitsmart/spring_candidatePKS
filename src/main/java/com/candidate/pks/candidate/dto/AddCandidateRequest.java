package com.candidate.pks.candidate.dto;
import com.candidate.pks.candidate.model.Status;
import lombok.Data;

import java.util.Date;

@Data
public class AddCandidateRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Status status;
    private String highSchoolPassOut;
    private String intermediatePassOut;
    private String bachelorPassOut;
    private String masterPassOut;
    private String cvUrl;
    private String candidateType;
    private Integer referralEmployeeId;
    private Date dob;
    private String address;
}
