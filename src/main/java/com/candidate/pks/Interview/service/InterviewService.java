package com.candidate.pks.Interview.service;

import com.candidate.pks.Interview.dto.ScheduledInterviewRequest;
import com.candidate.pks.Interview.model.Interview;
import com.candidate.pks.Interview.model.InterviewStatus;
import com.candidate.pks.Interview.repository.InterviewRepository;
import com.candidate.pks.candidate.model.Candidate;
import com.candidate.pks.candidate.repository.CandidateRepository;
import com.candidate.pks.auth.model.Employee;
import com.candidate.pks.auth.repository.EmployeeRepository;
import com.candidate.pks.exception.CandidateNotFoundException;
import com.candidate.pks.exception.EmployeeNotFoundException;
import com.candidate.pks.repeat.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

@Service
@RequiredArgsConstructor
public class InterviewService {

    private final CandidateRepository candidateRepository;
    private final EmployeeRepository employeeRepository;
    private final InterviewRepository interviewRepository;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");


    public Response createScheduled(ScheduledInterviewRequest scheduledInterviewRequest) {
        Candidate candidate = candidateRepository.findById(scheduledInterviewRequest.getCandidateId())
                .orElseThrow(() -> new CandidateNotFoundException("Candidate ID " + scheduledInterviewRequest.getCandidateId() + " not found"));

        Employee interviewer = employeeRepository.findById(scheduledInterviewRequest.getInterviewerId())
                .orElseThrow(() -> new EmployeeNotFoundException("Employee ID " + scheduledInterviewRequest.getInterviewerId() + " not found"));

        Interview interview = Interview.builder()
                .candidate(candidate)
                .interviewerName(interviewer)
                .interviewDate(scheduledInterviewRequest.getInterviewDate())
                .interviewStatus(InterviewStatus.SCHEDULED)
                .build();

        interviewRepository.save(interview);

        return new Response("Interview scheduled successfully.");
    }
}
