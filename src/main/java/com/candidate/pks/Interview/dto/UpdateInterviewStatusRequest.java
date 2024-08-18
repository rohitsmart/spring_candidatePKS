package com.candidate.pks.Interview.dto;

import com.candidate.pks.Interview.model.InterviewStatus;
import lombok.Data;

@Data
public class UpdateInterviewStatusRequest {
    private Integer interviewId;
    private InterviewStatus interviewStatus;

}
