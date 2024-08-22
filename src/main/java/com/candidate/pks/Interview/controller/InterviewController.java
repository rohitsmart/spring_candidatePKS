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
@Tag(name = "Interview Management", description = "Endpoints for managing interview scheduling, interviewer transfer, and interview status updates.")
public class InterviewController {

    private final InterviewService interviewService;

    @PostMapping("schedule")
    @Operation(
            summary = "Schedule Interview",
            description = "Schedule a new interview for a candidate. This endpoint is typically used by HR to organize the first round of interviews."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Interview scheduled successfully.",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data. Please verify the scheduling details."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. You do not have the necessary permissions to access this resource."),
            @ApiResponse(responseCode = "500", description = "Internal server error. An unexpected error occurred while processing the request.")
    })
    public ResponseEntity<Response> createScheduled(@RequestBody ScheduledInterviewRequest scheduledInterviewRequest) {
        Response response = interviewService.createOrUpdateScheduled(scheduledInterviewRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("transfer-interviewer")
    @Operation(
            summary = "Transfer Interviewer",
            description = "Transfer the responsibility of an ongoing interview to another interviewer. This is used to assign a different interviewer mid-process."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Interviewer transferred successfully.",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data. Please verify the interview ID and the new interviewer details."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. You do not have the necessary permissions to perform this action."),
            @ApiResponse(responseCode = "500", description = "Internal server error. An unexpected error occurred while processing the request.")
    })
    public ResponseEntity<Response> transferInterviewer(
            @RequestParam("interviewID") Integer interviewID,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Details of the new interviewer and the updated schedule.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ScheduledInterviewRequest.class))
            )
            @RequestBody ScheduledInterviewRequest scheduledInterviewRequest) {
        Response response = interviewService.transferInterviewer(interviewID, scheduledInterviewRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("first-information")
    @Operation(
            summary = "Submit Initial Interview Feedback",
            description = "Submit the initial feedback or commit records after conducting the first round of interviews with a candidate."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidate information updated successfully.",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data. Please verify the feedback details."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. You do not have the necessary permissions to perform this action."),
            @ApiResponse(responseCode = "500", description = "Internal server error. An unexpected error occurred while processing the request.")
    })
    public ResponseEntity<Response> updateCandidate(@RequestBody InitialCommitRequest initialCommitRequest) {
        Response response = interviewService.updateCandidate(initialCommitRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("update-interview-status")
    @Operation(
            summary = "Update Interview Status",
            description = "Update the status of a candidate's interview, such as marking it as completed, passed, or failed."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Interview status updated successfully.",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data. Please verify the status update details."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. You do not have the necessary permissions to perform this action."),
            @ApiResponse(responseCode = "500", description = "Internal server error. An unexpected error occurred while processing the request.")
    })
    public ResponseEntity<Response> updateInterviewStatus(@RequestBody UpdateInterviewStatusRequest updateInterviewStatusRequest) {
        Response response = interviewService.updateInterviewStatus(updateInterviewStatusRequest);
        return ResponseEntity.ok(response);
    }
}
