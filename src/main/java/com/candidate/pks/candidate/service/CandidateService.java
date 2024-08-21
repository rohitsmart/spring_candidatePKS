package com.candidate.pks.candidate.service;

import com.candidate.pks.candidate.dto.AddCandidateRequest;
import com.candidate.pks.candidate.dto.UpdateCandidateRequest;
import com.candidate.pks.candidate.model.Candidate;
import com.candidate.pks.candidate.repository.CandidateRepository;
import com.candidate.pks.auth.model.Employee;
import com.candidate.pks.auth.repository.EmployeeRepository;
import com.candidate.pks.exception.CandidateNotFoundException;
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
                throw new RuntimeException("Referal Employee Does Not Exist");
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
                .address(addCandidateRequest.getAddress())
                .bachelorDegree(addCandidateRequest.getBachelorDegree())
                .masterDegree(addCandidateRequest.getMasterDegree())
                .district(addCandidateRequest.getDistrict())
                .state(addCandidateRequest.getState())
                .dob(addCandidateRequest.getDob())
                .build();

        candidateRepository.save(candidate);

        return new Response("Candidate added successfully.");
    }


    public Response updateStatus(UpdateCandidateRequest updateCandidateRequest) {
        var candidate = candidateRepository.findByCandidateId(updateCandidateRequest.getCandidateId()).orElseThrow(
                ()-> new CandidateNotFoundException("Candidate not found exception")
        );
        candidate.setStatus(updateCandidateRequest.getStatus());
        candidateRepository.save(candidate);
        return Response.builder()
                .message("Candidate status updated")
                .build();
    }
}
