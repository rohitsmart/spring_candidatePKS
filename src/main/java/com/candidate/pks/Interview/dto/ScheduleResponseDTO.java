package com.candidate.pks.Interview.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ScheduleResponseDTO {
    private Integer interviewId;
    private String candidateId;
    private String candidateName;
    private String candidateType;
    private Date applicationDate;
    private Date interviewDate;
    private String interviewStatus;
    private String employeeId;
    private String employeeName;
}
