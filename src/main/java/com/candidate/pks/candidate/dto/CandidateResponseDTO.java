package com.candidate.pks.candidate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateResponseDTO {
    private String candidateId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String status;
    private String candidateType;
    private String referralEmployee;
}
