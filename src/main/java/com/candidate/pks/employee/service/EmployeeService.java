package com.candidate.pks.employee.service;

import com.candidate.pks.candidate.model.Candidate;
import com.candidate.pks.candidate.model.Status;
import com.candidate.pks.candidate.repository.CandidateRepository;
import com.candidate.pks.Interview.model.Interview;
import com.candidate.pks.Interview.model.InterviewStatus;
import com.candidate.pks.Interview.repository.InterviewRepository;
import com.candidate.pks.employee.dto.CandidateRequest;
import com.candidate.pks.employee.dto.CandidateResponse;
import com.candidate.pks.employee.dto.InterviewResponse;
import com.candidate.pks.employee.dto.ListOfCandidate;
import com.candidate.pks.repeat.Response;
import com.candidate.pks.security.UserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class EmployeeService {

    private final InterviewRepository interviewRepository;
    private final CandidateRepository candidateRepository;
    private final UserDetail userDetail;

    public ListOfCandidate fetchCandidates(Date fromDate, Date toDate, int page, int size) {
//        Integer interviewerId = userDetail.getUser().getEmployee().getId();
        Integer interviewerId = 1;

        PageRequest pageRequest = PageRequest.of(page, size);
        Date today = new Date();

        Page<Interview> interviewsPage;
        if (fromDate != null && toDate != null) {
            interviewsPage = interviewRepository.findByInterviewerIdAndInterviewDateBetweenAndInterviewStatus(interviewerId, fromDate, toDate, InterviewStatus.SCHEDULED, pageRequest);
        } else {
            interviewsPage = interviewRepository.findUpcomingInterviewsByInterviewerIdAndInterviewStatus(interviewerId, today, InterviewStatus.SCHEDULED, pageRequest);
        }

        List<Interview> interviews = interviewsPage.getContent();
        Map<Candidate, List<Interview>> candidateInterviewsMap = new HashMap<>();

        // Group interviews by candidate
        for (Interview interview : interviews) {
            Candidate candidate = interview.getCandidate();
            candidateInterviewsMap
                    .computeIfAbsent(candidate, k -> new ArrayList<>())
                    .add(interview);
        }

        List<CandidateResponse> candidateResponses = candidateInterviewsMap.entrySet().stream()
                .map(entry -> {
                    Candidate candidate = entry.getKey();
                    List<Interview> candidateInterviews = entry.getValue();

                    CandidateResponse response = new CandidateResponse();
                    response.setId(candidate.getId());
                    response.setCandidateId(candidate.getCandidateId());
                    response.setFirstName(candidate.getFirstName());
                    response.setLastName(candidate.getLastName());
                    response.setEmail(candidate.getEmail());
                    response.setPhone(candidate.getPhone());
                    response.setStatus(candidate.getStatus().name());
                    response.setHighSchoolPassOut(candidate.getHighSchoolPassOut());
                    response.setIntermediatePassOut(candidate.getIntermediatePassOut());
                    response.setBachelorPassOut(candidate.getBachelorPassOut());
                    response.setMasterPassOut(candidate.getMasterPassOut());
                    response.setCvUrl(candidate.getCvUrl());
                    response.setCandidateType(candidate.getCandidateType());

                    response.setDsaRating(candidate.getDsaRating());
                    response.setReactRating(candidate.getReactRating());
                    response.setJavascriptRating(candidate.getJavascriptRating());
                    response.setOopsRating(candidate.getOopsRating());
                    response.setSqlRating(candidate.getSqlRating());
                    response.setJavaRating(candidate.getJavaRating());
                    response.setPhpRating(candidate.getPhpRating());
                    response.setPythonRating(candidate.getPythonRating());
                    response.setHtmlRating(candidate.getHtmlRating());
                    response.setCssRating(candidate.getCssRating());
                    response.setBootstrapRating(candidate.getBootstrapRating());
                    response.setMaterialUiRating(candidate.getMaterialUiRating());
                    response.setTailwindCssRating(candidate.getTailwindCssRating());
                    response.setFlutterRating(candidate.getFlutterRating());
                    response.setReactNativeRating(candidate.getReactNativeRating());
                    response.setMachineLearning(candidate.getMachineLearning());

                    response.setCommunication(candidate.getCommunication());
                    response.setDressingSense(candidate.getDressingSense());
                    response.setOverAll(candidate.getOverAll());
                    response.setApplicationDate(candidate.getApplicationDate());

                    // Map interviews for this candidate
                    List<InterviewResponse> interviewResponses = candidateInterviews.stream()
                            .map(interview -> {
                                InterviewResponse interviewResponse = new InterviewResponse();
                                interviewResponse.setId(interview.getId());
                                interviewResponse.setInterviewDate(interview.getInterviewDate());
                                interviewResponse.setInterviewStatus(interview.getInterviewStatus());
                                interviewResponse.setFeedback(interview.getFeedback());
                                return interviewResponse;
                            })
                            .collect(Collectors.toList());
                    response.setInterview(interviewResponses);

                    return response;
                })
                .collect(Collectors.toList());

        ListOfCandidate listOfCandidate = new ListOfCandidate();
        listOfCandidate.setCandidates(candidateResponses);
        listOfCandidate.setTotalPages(interviewsPage.getTotalPages());
        listOfCandidate.setCurrentPage(interviewsPage.getNumber());
        return listOfCandidate;
    }

    public Response saveCandidateFirstRound(String candidateID, CandidateRequest candidateRequest) {
        var interview = interviewRepository.findById(candidateRequest.getInterviewId())
                .orElseThrow(() -> new RuntimeException("Interview not found with ID: " + candidateRequest.getInterviewId()));

        interview.setFeedback(candidateRequest.getFeedback());
        interview.setInterviewStatus(candidateRequest.getInterviewStatus());

        var candidate = interview.getCandidate();

        candidate.setDsaRating(candidateRequest.getDsaRating());
        candidate.setReactRating(candidateRequest.getReactRating());
        candidate.setJavascriptRating(candidateRequest.getJavascriptRating());
        candidate.setOopsRating(candidateRequest.getOopsRating());
        candidate.setSqlRating(candidateRequest.getSqlRating());
        candidate.setJavaRating(candidateRequest.getJavaRating());
        candidate.setPhpRating(candidateRequest.getPhpRating());
        candidate.setPythonRating(candidateRequest.getPythonRating());
        candidate.setHtmlRating(candidateRequest.getHtmlRating());
        candidate.setCssRating(candidateRequest.getCssRating());
        candidate.setBootstrapRating(candidateRequest.getBootstrapRating());
        candidate.setMaterialUiRating(candidateRequest.getMaterialUiRating());
        candidate.setTailwindCssRating(candidateRequest.getTailwindCssRating());
        candidate.setFlutterRating(candidateRequest.getFlutterRating());
        candidate.setReactNativeRating(candidateRequest.getReactNativeRating());
        candidate.setMachineLearning(candidateRequest.getMachineLearning());
        candidate.setStatus(Status.INTERVIEWED);

        candidateRepository.save(candidate);
        interviewRepository.save(interview);
        return new Response("Candidate and interview details updated successfully");
    }

}
