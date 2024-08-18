package com.candidate.pks.candidate.dto;

import com.candidate.pks.candidate.model.Status;
import lombok.Data;

@Data
public class UpdateCandidateRequest {
    private String candidateId;
    private Status status;

}
