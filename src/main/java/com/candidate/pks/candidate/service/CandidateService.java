package com.candidate.pks.candidate.service;

import com.candidate.pks.candidate.dto.AddCandidateRequest;
import com.candidate.pks.candidate.model.Candidate;
import com.candidate.pks.candidate.model.Status;
import com.candidate.pks.candidate.repository.CandidateRepository;
import com.candidate.pks.auth.model.Employee;
import com.candidate.pks.auth.repository.EmployeeRepository;
import com.candidate.pks.repeat.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final EmployeeRepository employeeRepository;

    public Response addCandidate(AddCandidateRequest addCandidateRequest) {

        Employee referralEmployee = null;
        if (addCandidateRequest.getReferralEmployeeId() != null) {
            referralEmployee = employeeRepository.findById(addCandidateRequest.getReferralEmployeeId())
                    .orElse(null);
            if (referralEmployee == null) {
                throw new RuntimeException("Referral Employee Does Not Exist");
            }
        }
        Candidate candidate = Candidate.builder()
                .firstName(addCandidateRequest.getFirstName())
                .lastName(addCandidateRequest.getLastName())
                .email(addCandidateRequest.getEmail())
                .phone(addCandidateRequest.getPhone())
                .status(addCandidateRequest.getStatus())
                .highSchoolPassOut(addCandidateRequest.getHighSchoolPassOut())
                .intermediatePassOut(addCandidateRequest.getIntermediatePassOut())
                .bachelorPassOut(addCandidateRequest.getBachelorPassOut())
                .masterPassOut(addCandidateRequest.getMasterPassOut())
                .cvUrl(addCandidateRequest.getCvUrl())
                .candidateType(addCandidateRequest.getCandidateType())
                .referralEmployee(referralEmployee)
                .build();

        candidateRepository.save(candidate);

        return new Response("Candidate added successfully.");
    }
}
