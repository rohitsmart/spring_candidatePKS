package com.candidate.pks.candidate.controller;

import com.candidate.pks.candidate.dto.AddCandidateRequest;
import com.candidate.pks.candidate.dto.CandidateResponseList;
import com.candidate.pks.candidate.dto.FetchCandidatesRequest;
import com.candidate.pks.candidate.dto.UpdateCandidateRequest;
import com.candidate.pks.candidate.model.Status;
import com.candidate.pks.candidate.service.CandidateService;
import com.candidate.pks.repeat.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/protected/candidate/")
@RequiredArgsConstructor
@Tag(name = "Candidate Management", description = "Endpoints for managing candidates, including adding and updating candidate information.")
public class CandidateController {

    private final CandidateService candidateService;

    @PostMapping("save")
    @Operation(
            summary = "Add New Candidate",
            description = "This endpoint allows HR personnel to add a new candidate to the system. The candidate's details, including name, experience, and contact information, are provided in the request body."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidate successfully added.",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input. Please verify the candidate details and try again."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. You do not have permission to perform this action."),
            @ApiResponse(responseCode = "500", description = "Internal server error. An unexpected error occurred while processing the request.")
    })
    public ResponseEntity<Response> addCandidate(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Candidate information to be added, including name, experience, and contact details.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AddCandidateRequest.class))
            )
            @RequestBody AddCandidateRequest addCandidateRequest
    ) {
        Response response = candidateService.addCandidate(addCandidateRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("update-status")
    @Operation(
            summary = "Update Candidate Status",
            description = "This endpoint allows HR personnel to update the status of a candidate, such as changing their interview status or hiring decision."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidate status successfully updated.",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input. Please verify the status update details and try again."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. You do not have permission to perform this action."),
            @ApiResponse(responseCode = "500", description = "Internal server error. An unexpected error occurred while processing the request.")
    })
    public ResponseEntity<Response> updateStatus(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Details of the candidate whose status is being updated, including the new status.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UpdateCandidateRequest.class))
            )
            @RequestBody UpdateCandidateRequest updateCandidateRequest
    ) {
        Response response = candidateService.updateStatus(updateCandidateRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/fetchAllCandidates")
    @Operation(
            summary = "Fetch All Candidates",
            description = "This endpoint allows you to fetch a list of candidates based on filters such as schedule, status, and pagination."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidates successfully fetched.",
                    content = @Content(schema = @Schema(implementation = CandidateResponseList.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input. Please verify the filter criteria and try again."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. You do not have permission to perform this action."),
            @ApiResponse(responseCode = "500", description = "Internal server error. An unexpected error occurred while processing the request.")
    })
    public ResponseEntity<CandidateResponseList> fetchAllCandidates(
            @RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
            @RequestParam(value = "status", required = false) Status status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "50") int size
    ) {
        FetchCandidatesRequest request = new FetchCandidatesRequest();
        request.setFromDate(fromDate);
        request.setStatus(status);

        CandidateResponseList response = candidateService.fetchAllCandidates(request, page, size);
        return ResponseEntity.ok(response);
    }


}
