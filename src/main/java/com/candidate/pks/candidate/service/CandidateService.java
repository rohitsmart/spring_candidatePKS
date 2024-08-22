package com.candidate.pks.candidate.service;

import com.candidate.pks.Interview.model.Interview;
import com.candidate.pks.Interview.repository.InterviewRepository;
import com.candidate.pks.auth.model.Designation;
import com.candidate.pks.auth.model.User;
import com.candidate.pks.auth.model.UserRole;
import com.candidate.pks.candidate.dto.*;
import com.candidate.pks.candidate.model.Candidate;
import com.candidate.pks.candidate.model.Status;
import com.candidate.pks.candidate.repository.CandidateRepository;
import com.candidate.pks.auth.model.Employee;
import com.candidate.pks.auth.repository.EmployeeRepository;
import com.candidate.pks.exception.BadDateAndTimeFormatException;
import com.candidate.pks.exception.CandidateNotFoundException;
import com.candidate.pks.repeat.Response;
import com.candidate.pks.service.MailService;
import com.candidate.pks.util.CandidateIdGenerator;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final EmployeeRepository employeeRepository;
    private final InterviewRepository interviewRepository;
    @Autowired
    private final CandidateIdGenerator candidateIdGenerator;
    private final MailService mailService;

    private static final String[] FIRST_NAMES = {"John", "Jane", "Michael", "Emily", "Robert", "Linda", "David", "Sarah", "James", "Jessica"};
    private static final String[] LAST_NAMES = {"Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor"};
    private static final String[] DEGREE = {"Computer Science", "Mechanical Engineering", "Electrical Engineering", "Business Administration", "Finance"};
    private static final String[] CV_URLS = {"http://example.com/cv/johndoe", "http://example.com/cv/janedoe", "http://example.com/cv/michaelbrown"};
    private static final String[] CANDIDATE_TYPES = {"front-end", "back-end", "fullstack"};
    private static final String[] DISTRICTS = {"Central", "East", "West", "North", "South"};
    private static final String[] STATES = {"California", "New York", "Texas", "Florida", "Illinois"};

    private static final Random RANDOM = new Random();


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
                .dob(dob)
                .build();

        candidateRepository.save(candidate);
        try {
            mailService.sendWelcomeEmail(
                    candidate.getEmail(),
                    candidate.getFirstName(),
                    candidate.getLastName()
            );
        } catch (MessagingException | IOException e) {
            log.error("Failed to send welcome email to candidate: {}", candidate.getEmail(), e);
            throw new RuntimeException("Failed to send welcome email to candidate.");
        }

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

    public CandidateResponseList fetchAllCandidates(FetchCandidatesRequest request, int page, int size, User user) {
        UserRole loginUserRole = user.getRole();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("applicationDate")));
        LocalDate fromDate = request.getFromDate();
        LocalDate toDate = LocalDate.now();
        Status status = request.getStatus();
        Page<Candidate> candidatePage;

        if (loginUserRole == UserRole.ADMIN) {
            if (fromDate != null && status != null) {
                candidatePage = candidateRepository.findByApplicationDateBetweenAndStatus(fromDate.atStartOfDay(), toDate.atTime(23, 59, 59), status, pageable);
            } else if (fromDate != null) {
                candidatePage = candidateRepository.findByApplicationDateBetween(fromDate.atStartOfDay(), toDate.atTime(23, 59, 59), pageable);
            } else if (status != null) {
                candidatePage = candidateRepository.findByStatus(status, pageable);
            } else {
                candidatePage = candidateRepository.findAll(pageable);
            }
        } else {
            Designation loginUserDesignation = user.getEmployee() != null ? user.getEmployee().getDesignation() : null;
            String loginUserEmpId = user.getEmployee() != null ? user.getEmployee().getEmpId() : null;
            if (loginUserRole == UserRole.Employee &&
                    (loginUserDesignation == Designation.HR || loginUserDesignation == Designation.MANAGER)) {
                if (fromDate != null && status != null) {
                    candidatePage = candidateRepository.findByApplicationDateBetweenAndStatus(fromDate.atStartOfDay(), toDate.atTime(23, 59, 59), status, pageable);
                } else if (fromDate != null) {
                    candidatePage = candidateRepository.findByApplicationDateBetween(fromDate.atStartOfDay(), toDate.atTime(23, 59, 59), pageable);
                } else if (status != null) {
                    candidatePage = candidateRepository.findByStatus(status, pageable);
                } else {
                    candidatePage = candidateRepository.findAll(pageable);
                }
            } else {
                candidatePage = candidateRepository.findByInterviewScheduledForEmpId(loginUserEmpId, pageable);
            }
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        List<CandidateResponseDTO> candidates = candidatePage.getContent().stream()
                .map(candidate -> {
                    String referralEmployeeInfo = null;
                    if (candidate.getReferralEmployee() != null) {
                        String empId = candidate.getReferralEmployee().getEmpId();
                        String firstName = candidate.getReferralEmployee().getFirstName();
                        String lastName = candidate.getReferralEmployee().getLastName();
                        referralEmployeeInfo = empId + " " + firstName + " " + lastName;
                    }

                    LocalDateTime applicationDateTime = candidate.getApplicationDate().toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime();
                    String formattedApplicationDate = applicationDateTime.format(formatter);

                    // Default values for interview details
                    String interviewDate = null;
                    boolean isScheduled = false;

                    if (candidate.getStatus() == Status.INTERVIEW_SCHEDULED) {
                        // Fetch the interview details for the candidate
                        Optional<Interview> interviewOpt = interviewRepository.findByCandidate(candidate);
                        if (interviewOpt.isPresent()) {
                            Interview interview = interviewOpt.get();
                            LocalDateTime interviewDateTime = interview.getInterviewDate().toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDateTime();
                            interviewDate = interviewDateTime.format(formatter);
                            isScheduled = true;
                        }
                    }

                    return new CandidateResponseDTO(
                            candidate.getCandidateId(),
                            candidate.getFirstName(),
                            candidate.getLastName(),
                            candidate.getEmail(),
                            candidate.getPhone(),
                            candidate.getStatus().name(),
                            candidate.getCandidateType(),
                            referralEmployeeInfo,
                            formattedApplicationDate,
                            interviewDate,
                            isScheduled
                    );
                })
                .collect(Collectors.toList());
        CandidateResponseList responseList = new CandidateResponseList();
        responseList.setCandidates(candidates);
        responseList.setTotalCandidates(candidatePage.getTotalElements());
        responseList.setTotalPages(candidatePage.getTotalPages());
        responseList.setCurrentPage(candidatePage.getNumber());

        return responseList;
    }


    public void generateDummyCandidates(int count) {
        for (int i = 0; i < count; i++) {
            AddCandidateRequest request = new AddCandidateRequest();
            request.setFirstName(FIRST_NAMES[RANDOM.nextInt(FIRST_NAMES.length)]);
            request.setLastName(LAST_NAMES[RANDOM.nextInt(LAST_NAMES.length)]);
            request.setEmail(request.getFirstName().toLowerCase() + "." + request.getLastName().toLowerCase() + "@example.com");
            request.setPhone("12345678" + RANDOM.nextInt(10));
            request.setStatus(Status.APPLICATION_RECEIVED);  // Or any other status as required
            request.setHighSchoolPassOut("200" + (RANDOM.nextInt(10) + 1));
            request.setIntermediatePassOut("201" + (RANDOM.nextInt(10) + 1));
            request.setBachelorDegree(DEGREE[RANDOM.nextInt(DEGREE.length)]);
            request.setBachelorPassOut("201" + (RANDOM.nextInt(10) + 1));
            request.setMasterDegree(DEGREE[RANDOM.nextInt(DEGREE.length)]);
            request.setMasterPassOut("201" + (RANDOM.nextInt(10) + 1));
            request.setCvUrl(CV_URLS[RANDOM.nextInt(CV_URLS.length)]);
            request.setCandidateType(CANDIDATE_TYPES[RANDOM.nextInt(CANDIDATE_TYPES.length)]);
            request.setDob("15/08/" + (RANDOM.nextInt(30) + 1970));
            request.setDistrict(DISTRICTS[RANDOM.nextInt(DISTRICTS.length)]);
            request.setState(STATES[RANDOM.nextInt(STATES.length)]);
            request.setAddress("123 Sample Address " + (RANDOM.nextInt(100) + 1));
            addCandidate(request);
        }
    }

}
