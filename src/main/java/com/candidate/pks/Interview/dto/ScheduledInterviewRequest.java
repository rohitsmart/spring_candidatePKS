package com.candidate.pks.Interview.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ScheduledInterviewRequest {
    private String candidateId;
    private String interviewerId;
    private String interviewDate;
}
