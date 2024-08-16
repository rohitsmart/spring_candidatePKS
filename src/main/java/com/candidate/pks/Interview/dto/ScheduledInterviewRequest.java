package com.candidate.pks.Interview.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ScheduledInterviewRequest {
    private Integer candidateId;
    private Integer interviewerId;
    private Date interviewDate;
}
