package com.candidate.pks.Interview.service;

import com.candidate.pks.Interview.dto.ScheduledInterviewRequest;
import com.candidate.pks.Interview.dto.UpdateInterviewStatusRequest;
import com.candidate.pks.Interview.model.Interview;
import com.candidate.pks.Interview.model.InterviewStatus;
import com.candidate.pks.Interview.repository.InterviewRepository;
import com.candidate.pks.Interview.dto.InitialCommitRequest;
import com.candidate.pks.candidate.model.Candidate;
import com.candidate.pks.candidate.repository.CandidateRepository;
import com.candidate.pks.auth.model.Employee;
import com.candidate.pks.auth.repository.EmployeeRepository;
import com.candidate.pks.exception.CandidateNotFoundException;
import com.candidate.pks.exception.EmployeeNotFoundException;
import com.candidate.pks.repeat.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class InterviewService {

    private final CandidateRepository candidateRepository;
    private final EmployeeRepository employeeRepository;
    private final InterviewRepository interviewRepository;
    public Response createScheduled(ScheduledInterviewRequest scheduledInterviewRequest) {
        // Define the date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        // Parse the date string to a Date object
        Date interviewDate;
        try {
            interviewDate = dateFormat.parse(scheduledInterviewRequest.getInterviewDate());
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use DD/MM/YYYY HH:MM.");
        }

        Candidate candidate = candidateRepository.findByCandidateId(scheduledInterviewRequest.getCandidateId())
                .orElseThrow(() -> new CandidateNotFoundException("Candidate ID " + scheduledInterviewRequest.getCandidateId() + " not found"));

        Employee interviewer = employeeRepository.findByEmpId(scheduledInterviewRequest.getInterviewerId())
                .orElseThrow(() -> new EmployeeNotFoundException("Employee ID " + scheduledInterviewRequest.getInterviewerId() + " not found"));

        Interview interview = Interview.builder()
                .candidate(candidate)
                .interviewerName(interviewer)
                .interviewDate(interviewDate)
                .interviewStatus(InterviewStatus.SCHEDULED)
                .build();

        interviewRepository.save(interview);

        return new Response("Interview scheduled successfully.");
    }
    public Response updateCandidate(InitialCommitRequest initialCommitRequest) {
        var candidate = candidateRepository.findByCandidateId(initialCommitRequest.getCandidateId()).orElseThrow(
                ()-> new CandidateNotFoundException("candidate with id "+initialCommitRequest.getCandidateId()+" does not exist")
        );

        candidate.setCommunication(initialCommitRequest.getCommunication());
        candidate.setDressingSense(initialCommitRequest.getDressingSense());
        candidate.setOverAll(initialCommitRequest.getOverAll());
        candidateRepository.save(candidate);
        return new Response("candidate first information updated");
    }

    public Response transferInterviewer(Integer interviewID, ScheduledInterviewRequest scheduledInterviewRequest) {
        var interview= interviewRepository.findById(interviewID).orElseThrow(()-> new RuntimeException("Error occur"));
        Employee interviewer = employeeRepository.findByEmpId(scheduledInterviewRequest.getInterviewerId())
                .orElseThrow(() -> new EmployeeNotFoundException("Employee ID " + scheduledInterviewRequest.getInterviewerId() + " not found"));
        interview.setInterviewerName(interviewer);
        interviewRepository.save(interview);
        return new Response("Interviewer Transfer successfully.");

    }

    public Response updateInterviewStatus(UpdateInterviewStatusRequest updateInterviewStatusRequest) {
        var interview= interviewRepository.findById(updateInterviewStatusRequest.getInterviewId()).orElseThrow(()-> new RuntimeException("Error"));
        interview.setInterviewStatus(updateInterviewStatusRequest.getInterviewStatus());
        interviewRepository.save(interview);
        return new Response("Interview Updated successfully.");
    }
}
