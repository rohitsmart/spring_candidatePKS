package com.candidate.pks.Interview.dto;

import com.candidate.pks.Interview.model.InterviewStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FetchScheduleRequest {
    private LocalDate fromDate;
    private InterviewStatus interviewStatus;
}
