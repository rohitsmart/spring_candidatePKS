package com.candidate.pks.employee.dto;

import lombok.Data;
import java.util.List;

@Data
public class ListOfCandidate {
    private List<CandidateResponse> candidates;
    private int totalPages;
    private int currentPage;
}
