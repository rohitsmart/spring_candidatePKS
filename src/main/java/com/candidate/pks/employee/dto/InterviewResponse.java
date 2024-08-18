package com.candidate.pks.employee.dto;


import com.candidate.pks.Interview.model.InterviewStatus;
import lombok.Data;

import java.util.Date;

@Data
public class InterviewResponse {
    private Integer id;
    private Date interviewDate;
    private InterviewStatus interviewStatus;
    private String feedback;
}
