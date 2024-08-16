package com.candidate.pks.Interview.model;


import com.candidate.pks.auth.model.Employee;
import com.candidate.pks.candidate.model.Candidate;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "interviewer_name_id")
    private Employee interviewerName;

    @Temporal(TemporalType.TIMESTAMP)
    private Date interviewDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InterviewStatus interviewStatus;

    private String feedback;

}
