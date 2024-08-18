package com.candidate.pks.Interview.controller;

import com.candidate.pks.Interview.dto.ScheduledInterviewRequest;
import com.candidate.pks.Interview.dto.UpdateInterviewStatusRequest;
import com.candidate.pks.Interview.service.InterviewService;
import com.candidate.pks.auth.dto.LoginResponse;
import com.candidate.pks.Interview.dto.InitialCommitRequest;
import com.candidate.pks.repeat.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/protected/interview/")
@RequiredArgsConstructor
@Tag(name = "Interview", description = "APIs related to Interview Management")
public class InterviewController {

    private final InterviewService interviewService;

    // to be filled by Hr //

    // will schedule first round interview
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
    // to be filled by Hr //
    @PostMapping("transfer-interviewer")
    @Operation(summary = "schedule", description = "Add scheduled ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Added Employee", content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Response> transferInterviewer(@RequestParam("interviewID") Integer interviewID, @RequestBody ScheduledInterviewRequest scheduledInterviewRequest) {
        Response response = interviewService.transferInterviewer(interviewID,scheduledInterviewRequest);
        return ResponseEntity.ok(response);
    }

    // to be filled by Hr //
    @PostMapping("first-information")
    @Operation(summary = "update  candidate", description = "update Candidate ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful updated Employee", content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Response> updateCandidate(@RequestBody InitialCommitRequest initialCommitRequest) {
        Response response = interviewService.updateCandidate(initialCommitRequest);
        return ResponseEntity.ok(response);
    }

    // to be filled by Hr //
    // update interview status;
    @PostMapping("update-interview-status")
    @Operation(summary = "update  candidate", description = "update Candidate ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful updated Employee", content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Response> updateInterviewStatus(@RequestBody UpdateInterviewStatusRequest updateInterviewStatusRequest) {
        Response response = interviewService.updateInterviewStatus(updateInterviewStatusRequest);
        return ResponseEntity.ok(response);
    }
}
