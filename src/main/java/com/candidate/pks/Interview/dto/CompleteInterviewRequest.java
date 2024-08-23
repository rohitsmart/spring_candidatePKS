package com.candidate.pks.Interview.dto;

import com.candidate.pks.Interview.model.InterviewStatus;
import lombok.Data;

import java.util.Date;

@Data
public class CompleteInterviewRequest {
    private String candidateId;
    private int dsaRating;
    private int reactRating;
    private int javascriptRating;
    private int oopsRating;
    private int sqlRating;
    private int javaRating;
    private int phpRating;
    private int pythonRating;
    private int htmlRating;
    private int cssRating;
    private int bootstrapRating;
    private int materialUiRating;
    private int tailwindCssRating;
    private int flutterRating;
    private int reactNativeRating;
    private int machineLearning;
    private Integer interviewId;
    private InterviewStatus interviewStatus;
    private String feedback;
}
