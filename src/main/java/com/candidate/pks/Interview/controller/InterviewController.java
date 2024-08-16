package com.candidate.pks.Interview.controller;

import com.candidate.pks.Interview.dto.ScheduledInterviewRequest;
import com.candidate.pks.Interview.service.InterviewService;
import com.candidate.pks.auth.dto.LoginResponse;
import com.candidate.pks.candidate.dto.AddCandidateRequest;
import com.candidate.pks.repeat.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/protected/interview/")
@RequiredArgsConstructor
@Tag(name = "Interview", description = "APIs related to Interview Management")
public class InterviewController {

    private final InterviewService interviewService;

    @PostMapping("schedule")
    @Operation(summary = "schedule", description = "Add scheduled ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Added Employee", content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Response> createScheduled(@RequestBody ScheduledInterviewRequest scheduledInterviewRequest) {
        Response response = interviewService.createScheduled(scheduledInterviewRequest);
        return ResponseEntity.ok(response);
    }
}
