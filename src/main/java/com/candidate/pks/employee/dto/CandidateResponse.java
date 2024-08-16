package com.candidate.pks.employee.dto;

import lombok.Data;
import java.util.Date;
@Data
public class CandidateResponse {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String status;
    private String highSchoolPassOut;
    private String intermediatePassOut;
    private String bachelorPassOut;
    private String masterPassOut;
    private String cvUrl;
    private String candidateType; // front-end, back-end, fullstack
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
    private String communication;
    private String dressingSense;
    private String overAll;
    private Date applicationDate;
    private InterviewResponse interview;
}
