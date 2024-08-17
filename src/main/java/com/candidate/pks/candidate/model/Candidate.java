package com.candidate.pks.candidate.model;

import com.candidate.pks.Interview.model.Interview;
import com.candidate.pks.auth.model.Employee;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true,nullable = false)
    private String candidateId;

    @PrePersist
    private void generateCandidateId() {
        if (this.candidateId == null) {
            this.candidateId = "CD" + String.format("%04d", this.id);
        }
    }

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String highSchoolPassOut;
    private String intermediatePassOut;
    private String bachelorPassOut;
    private String masterPassOut;

    private String cvUrl;
    private String candidateType;// front-end, back-end, fullstack,

    @ManyToOne
    @JoinColumn(name = "referral_employee_id")
    private Employee referralEmployee;


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
    /** Start things to be filled by HR **/
    private String communication;
    private String dressingSense;
    private String overAll;
    /** End things to be filled by HR **/

    @Temporal(TemporalType.TIMESTAMP)
    private Date applicationDate;

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Interview> interviews;

    @PrePersist
    protected void onCreate() {
        applicationDate = new Date();
        status= Status.APPLIED;
    }
}

