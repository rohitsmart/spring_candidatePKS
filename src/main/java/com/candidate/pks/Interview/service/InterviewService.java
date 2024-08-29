package com.candidate.pks.Interview.service;

import com.candidate.pks.Interview.dto.*;
import com.candidate.pks.Interview.model.Interview;
import com.candidate.pks.Interview.model.InterviewStatus;
import com.candidate.pks.Interview.repository.InterviewRepository;
import com.candidate.pks.auth.model.User;
import com.candidate.pks.auth.model.UserRole;
import com.candidate.pks.candidate.model.Candidate;
import com.candidate.pks.candidate.model.Status;
import com.candidate.pks.candidate.repository.CandidateRepository;
import com.candidate.pks.auth.model.Employee;
import com.candidate.pks.auth.repository.EmployeeRepository;
import com.candidate.pks.exception.CandidateNotFoundException;
import com.candidate.pks.exception.EmployeeNotFoundException;
import com.candidate.pks.repeat.Response;
import com.candidate.pks.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterviewService {

    private final CandidateRepository candidateRepository;
    private final EmployeeRepository employeeRepository;
    private final InterviewRepository interviewRepository;
    private final MailService mailService;
    public Response createOrUpdateScheduled(ScheduledInterviewRequest scheduledInterviewRequest) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
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

        // Check if an interview already exists
        Optional<Interview> existingInterview = interviewRepository.findByCandidateAndInterviewerName(candidate, interviewer);

        if (existingInterview.isPresent()) {
            // Update the existing interview
            Interview interview = existingInterview.get();
            interview.setInterviewDate(interviewDate);
            interview.setInterviewStatus(InterviewStatus.SCHEDULED);
            interviewRepository.save(interview);

            // Send email notifications
            sendNotificationEmails(candidate, interviewer, interviewDate);

            return new Response("Interview updated successfully.");
        }  else {
            // Create a new interview
            Interview newInterview = Interview.builder()
                    .candidate(candidate)
                    .interviewerName(interviewer)
                    .interviewDate(interviewDate)
                    .interviewStatus(InterviewStatus.SCHEDULED)
                    .build();

            interviewRepository.save(newInterview);
            candidate.setStatus(Status.INTERVIEW_SCHEDULED);
            candidateRepository.save(candidate);

            // Send email notifications
            sendNotificationEmails(candidate, interviewer, interviewDate);

            return new Response("Interview scheduled successfully.");
        }
    }

    private void sendNotificationEmails(Candidate candidate, Employee interviewer, Date interviewDate) {
        try {
            // Send email to candidate
            mailService.sendInterviewNotificationToCandidate(
                    candidate.getEmail(),
                    candidate.getFirstName(),
                    new SimpleDateFormat("dd MMM yyyy HH:mm").format(interviewDate),
                    interviewer.getFirstName() + " " + interviewer.getLastName()
            );

            // Send email to interviewer
            mailService.sendInterviewNotificationToEmployee(
                    interviewer.getUser().getUsername(),
                    interviewer.getFirstName(),
                    new SimpleDateFormat("dd MMM yyyy HH:mm").format(interviewDate),
                    candidate.getFirstName() + " " + candidate.getLastName()
            );
        } catch (MessagingException | IOException e) {
            throw new RuntimeException("Failed to send interview notification emails", e);
        }
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

    public ScheduleResponseList fetchAllSchedule(FetchScheduleRequest request, int page, int size, User user) {
        Pageable pageable = PageRequest.of(page, size);
        Date fromDate = null;

        if (request.getFromDate() != null) {
            fromDate = Date.from(request.getFromDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
        }

        Page<Interview> interviewPage;

        // Check user roles
        Set<UserRole> userRoles = user.getRoles();
        boolean isAdmin = userRoles.contains(UserRole.ADMIN);
        boolean isHR = userRoles.contains(UserRole.HR);
        boolean isManager = user.getEmployee() != null && "MANAGER".equalsIgnoreCase(user.getEmployee().getDesignation());

        if (isAdmin || isHR || isManager) {
            // Admin, HR, or Manager can see all interviews
            if (fromDate != null && request.getInterviewStatus() != null) {
                interviewPage = interviewRepository.findByInterviewDateAndStatus(fromDate, request.getInterviewStatus(), pageable);
            } else if (fromDate != null) {
                interviewPage = interviewRepository.findByInterviewDate(fromDate, pageable);
            } else if (request.getInterviewStatus() != null) {
                interviewPage = interviewRepository.findByInterviewStatus(request.getInterviewStatus(), pageable);
            } else {
                interviewPage = interviewRepository.findAll(pageable);
            }
        } else {
            // For other users, fetch interviews assigned to them
            if (fromDate != null && request.getInterviewStatus() != null) {
                assert user.getEmployee() != null;
                interviewPage = interviewRepository.findByInterviewerIdAndInterviewDateAndStatus(user.getEmployee().getId(), fromDate, request.getInterviewStatus(), pageable);
            } else if (fromDate != null) {
                assert user.getEmployee() != null;
                interviewPage = interviewRepository.findByInterviewerIdAndInterviewDate(user.getEmployee().getId(), fromDate, pageable);
            } else if (request.getInterviewStatus() != null) {
                assert user.getEmployee() != null;
                interviewPage = interviewRepository.findByInterviewerIdAndInterviewStatus(user.getEmployee().getId(), request.getInterviewStatus(), pageable);
            } else {
                assert user.getEmployee() != null;
                interviewPage = interviewRepository.findByInterviewerId(user.getEmployee().getId(), pageable);
            }
        }

        List<ScheduleResponseDTO> scheduleResponseDTOs = interviewPage.getContent().stream().map(interview -> {
            ScheduleResponseDTO dto = new ScheduleResponseDTO();
            dto.setInterviewId(interview.getId());
            dto.setCandidateId(interview.getCandidate().getCandidateId());
            dto.setCandidateName(interview.getCandidate().getFirstName() + " " + interview.getCandidate().getLastName());
            dto.setCandidateType(interview.getCandidate().getCandidateType());
            dto.setApplicationDate(interview.getCandidate().getApplicationDate());
            dto.setInterviewDate(interview.getInterviewDate());
            dto.setInterviewStatus(interview.getInterviewStatus().name());

            if (interview.getInterviewerName() != null) {
                dto.setEmployeeId(interview.getInterviewerName().getEmpId());
                dto.setEmployeeName(interview.getInterviewerName().getFirstName() + " " + interview.getInterviewerName().getLastName());
            }

            return dto;
        }).collect(Collectors.toList());

        ScheduleResponseList responseList = new ScheduleResponseList();
        responseList.setData(true);
        responseList.setScheduleResponseDTO(scheduleResponseDTOs);

        return responseList;
    }

    public InterviewResponseDTO fetchInterviewForCandidate(String candidateId, User user) {
        Interview interview = interviewRepository.findByCandidate_CandidateId(candidateId)
                .orElseThrow(() -> new RuntimeException("Interview does not exist for this candidate"));

        Candidate candidate = interview.getCandidate();
        Employee interviewer = interview.getInterviewerName();

        InterviewResponseDTO responseDTO = new InterviewResponseDTO();
        responseDTO.setCandidateId(candidate.getCandidateId());
        responseDTO.setFirstName(candidate.getFirstName());
        responseDTO.setLastName(candidate.getLastName());
        responseDTO.setEmail(candidate.getEmail());
        responseDTO.setPhone(candidate.getPhone());
        responseDTO.setHighSchoolPassOut(candidate.getHighSchoolPassOut());
        responseDTO.setIntermediatePassOut(candidate.getIntermediatePassOut());
        responseDTO.setBachelorDegree(candidate.getBachelorDegree());
        responseDTO.setBachelorPassOut(candidate.getBachelorPassOut());
        responseDTO.setMasterDegree(candidate.getMasterDegree());
        responseDTO.setMasterPassOut(candidate.getMasterPassOut());
        responseDTO.setCandidateType(candidate.getCandidateType());
        responseDTO.setCommunication(candidate.getCommunication());
        responseDTO.setDressingSense(candidate.getDressingSense());
        responseDTO.setOverAll(candidate.getOverAll());
        responseDTO.setDistrict(candidate.getDistrict());
        responseDTO.setState(candidate.getState());
        responseDTO.setAddress(candidate.getAddress());
        responseDTO.setDsaRating(candidate.getDsaRating());
        responseDTO.setReactRating(candidate.getReactRating());
        responseDTO.setJavascriptRating(candidate.getJavascriptRating());
        responseDTO.setOopsRating(candidate.getOopsRating());
        responseDTO.setSqlRating(candidate.getSqlRating());
        responseDTO.setJavaRating(candidate.getJavaRating());
        responseDTO.setPhpRating(candidate.getPhpRating());
        responseDTO.setPythonRating(candidate.getPythonRating());
        responseDTO.setHtmlRating(candidate.getHtmlRating());
        responseDTO.setCssRating(candidate.getCssRating());
        responseDTO.setBootstrapRating(candidate.getBootstrapRating());
        responseDTO.setMaterialUiRating(candidate.getMaterialUiRating());
        responseDTO.setTailwindCssRating(candidate.getTailwindCssRating());
        responseDTO.setFlutterRating(candidate.getFlutterRating());
        responseDTO.setReactNativeRating(candidate.getReactNativeRating());
        responseDTO.setMachineLearning(candidate.getMachineLearning());
        responseDTO.setInterviewId(interview.getId());
        responseDTO.setInterviewerName(interviewer.getFirstName() + " " + interviewer.getLastName());
        responseDTO.setInterviewDate(interview.getInterviewDate());
        responseDTO.setInterviewStatus(interview.getInterviewStatus().name());
        responseDTO.setFeedback(interview.getFeedback());

        return responseDTO;
    }

    @Transactional
    public Response completeInterview(CompleteInterviewRequest completeInterviewRequest) {
        try {
            var interview = interviewRepository.findById(completeInterviewRequest.getInterviewId())
                    .orElseThrow(() -> new RuntimeException("No record found"));

            var candidate = interview.getCandidate();
            if (candidate == null) {
                throw new RuntimeException("No candidate associated with this interview");
            }

            candidate.setDsaRating(completeInterviewRequest.getDsaRating());
            candidate.setReactRating(completeInterviewRequest.getReactRating());
            candidate.setJavascriptRating(completeInterviewRequest.getJavascriptRating());
            candidate.setOopsRating(completeInterviewRequest.getOopsRating());
            candidate.setSqlRating(completeInterviewRequest.getSqlRating());
            candidate.setJavaRating(completeInterviewRequest.getJavaRating());
            candidate.setPhpRating(completeInterviewRequest.getPhpRating());
            candidate.setPythonRating(completeInterviewRequest.getPythonRating());
            candidate.setHtmlRating(completeInterviewRequest.getHtmlRating());
            candidate.setCssRating(completeInterviewRequest.getCssRating());
            candidate.setBootstrapRating(completeInterviewRequest.getBootstrapRating());
            candidate.setMaterialUiRating(completeInterviewRequest.getMaterialUiRating());
            candidate.setTailwindCssRating(completeInterviewRequest.getTailwindCssRating());
            candidate.setFlutterRating(completeInterviewRequest.getFlutterRating());
            candidate.setReactNativeRating(completeInterviewRequest.getReactNativeRating());
            candidate.setMachineLearning(completeInterviewRequest.getMachineLearning());
            candidate.setStatus(Status.INTERVIEW_COMPLETED);
            interview.setInterviewStatus(completeInterviewRequest.getInterviewStatus());
            interview.setFeedback(completeInterviewRequest.getFeedback());
            candidateRepository.save(candidate);
            interviewRepository.save(interview);
            return new Response("Interview completed successfully");
        } catch (Exception e) {
            return new Response("Error completing interview: " + e.getMessage());
        }
    }
}
