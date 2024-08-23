package com.candidate.pks.Interview.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class InterviewResponseDTO {
    private String candidateId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String highSchoolPassOut;
    private String intermediatePassOut;
    private String bachelorDegree;
    private String bachelorPassOut;
    private String masterDegree;
    private String masterPassOut;
    private String candidateType;
    private String communication;
    private String dressingSense;
    private String overAll;
    private String district;
    private String state;
    private String address;
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
    private String interviewerName;
    private Date interviewDate;
    private String interviewStatus;
    private String feedback;

}

