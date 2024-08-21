package com.candidate.pks.candidate.dto;

import lombok.Data;
import java.util.List;

@Data
public class CandidateResponseList {
    private List<CandidateResponseDTO> candidates;
    private long totalCandidates;
    private int totalPages;
    private int currentPage;
}
