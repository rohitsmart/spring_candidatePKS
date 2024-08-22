package com.candidate.pks.candidate.service;

import com.candidate.pks.candidate.dto.*;
import com.candidate.pks.candidate.model.Candidate;
import com.candidate.pks.candidate.model.Status;
import com.candidate.pks.candidate.repository.CandidateRepository;
import com.candidate.pks.auth.model.Employee;
import com.candidate.pks.auth.repository.EmployeeRepository;
import com.candidate.pks.exception.BadDateAndTimeFormatException;
import com.candidate.pks.exception.CandidateNotFoundException;
import com.candidate.pks.repeat.Response;
import com.candidate.pks.util.CandidateIdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final EmployeeRepository employeeRepository;
    @Autowired
    private final CandidateIdGenerator candidateIdGenerator;

    public Response addCandidate(AddCandidateRequest addCandidateRequest) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dob;
        try {
            dob = LocalDate.parse(addCandidateRequest.getDob(), formatter);
        } catch (DateTimeParseException e) {
            log.error("Invalid date format for dob: {}", addCandidateRequest.getDob());
            throw new BadDateAndTimeFormatException("Invalid date format for dob. Please use dd/MM/yyyy.");
        }

        Employee referralEmployee = null;
        if (addCandidateRequest.getReferralEmployeeId() != null && addCandidateRequest.getReferralEmployeeId() != 0) {
            referralEmployee = employeeRepository.findByEmpId(String.valueOf(addCandidateRequest.getReferralEmployeeId()))
                    .orElse(null);
            if (referralEmployee == null) {
                throw new RuntimeException("Referral Employee Does Not Exist");
            }
        }
        String candidateId = candidateIdGenerator.generateCandidateId();

        Candidate candidate = Candidate.builder()
                .candidateId(candidateId)
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
                .dob(dob) // Use the parsed LocalDate here
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
    
    public CandidateResponseList fetchAllCandidates(FetchCandidatesRequest request, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        LocalDate fromDate = request.getFromDate();
        LocalDate toDate = LocalDate.now();
        Status status = request.getStatus();

        Page<Candidate> candidatePage;
        if (fromDate != null && status != null) {
            candidatePage = candidateRepository.findByApplicationDateBetweenAndStatus(fromDate.atStartOfDay(), toDate.atTime(23, 59, 59), status, pageable);
        } else if (fromDate != null) {
            candidatePage = candidateRepository.findByApplicationDateBetween(fromDate.atStartOfDay(), toDate.atTime(23, 59, 59), pageable);
        } else if (status != null) {
            candidatePage = candidateRepository.findByStatus(status, pageable);
        } else {
            candidatePage = candidateRepository.findAll(pageable);
        }

        List<CandidateResponseDTO> candidates = candidatePage.getContent().stream()
                .map(candidate -> {
                    String referralEmployeeInfo = null;
                    if (candidate.getReferralEmployee() != null) {
                        String empId = candidate.getReferralEmployee().getEmpId();
                        String firstName = candidate.getReferralEmployee().getFirstName();
                        String lastName = candidate.getReferralEmployee().getLastName();
                        referralEmployeeInfo = empId + " " + firstName + " " + lastName;
                    }

                    String formattedApplicationDate = candidate.getApplicationDate().toString();

                    return new CandidateResponseDTO(
                            candidate.getCandidateId(),
                            candidate.getFirstName(),
                            candidate.getLastName(),
                            candidate.getEmail(),
                            candidate.getPhone(),
                            candidate.getStatus().name(),
                            candidate.getCandidateType(),
                            referralEmployeeInfo,
                            formattedApplicationDate
                    );
                })
                .toList();

        CandidateResponseList responseList = new CandidateResponseList();
        responseList.setCandidates(candidates);
        responseList.setTotalCandidates(candidatePage.getTotalElements());
        responseList.setTotalPages(candidatePage.getTotalPages());
        responseList.setCurrentPage(candidatePage.getNumber());

        return responseList;
    }

}
