package com.candidate.pks.candidate.dto;

import com.candidate.pks.candidate.model.Status;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FetchCandidatesRequest {
    private LocalDate fromDate;
    private Status status;
}
