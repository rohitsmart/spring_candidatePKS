package com.candidate.pks.employee.controller;

import com.candidate.pks.employee.dto.CandidateRequest;
import com.candidate.pks.employee.dto.ListOfCandidate;
import com.candidate.pks.employee.service.EmployeeService;
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

import java.util.Date;

@RestController
@RequestMapping("/api/protected/employee/")
@RequiredArgsConstructor
@Tag(name = "Employee Management", description = "Endpoints for managing employee and candidate-related operations.")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("fetch-candidate")
    @Operation(
            summary = "Fetch Candidates with Scheduled Interviews",
            description = "Retrieve a list of candidates along with their scheduled interview details, filtered by interview date range. Supports pagination for large datasets."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidates and interview details fetched successfully.",
                    content = @Content(schema = @Schema(implementation = ListOfCandidate.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data. Please verify the date range and pagination parameters."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. You do not have the necessary permissions to access this resource."),
            @ApiResponse(responseCode = "500", description = "Internal server error. An unexpected error occurred while processing the request.")
    })
    public ResponseEntity<ListOfCandidate> fetchCandidate(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(employeeService.fetchCandidates(fromDate, toDate, page, size));
    }

    @PostMapping("candidate-first-round")
    @Operation(
            summary = "Save First Round Interview Feedback",
            description = "Submit feedback for the first round of interviews conducted with a candidate. This information is recorded by the employee who conducted the interview."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Feedback for the candidate's first round of interview saved successfully.",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data. Please verify the candidate ID and feedback details."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. You do not have the necessary permissions to perform this action."),
            @ApiResponse(responseCode = "500", description = "Internal server error. An unexpected error occurred while processing the request.")
    })
    public ResponseEntity<Response> saveCandidateFirstRound(
            @RequestParam("candidateId") String candidateID,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Feedback details for the candidate's first-round interview, including performance and recommendations.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CandidateRequest.class))
            )
            @RequestBody CandidateRequest candidateRequest) {
        return ResponseEntity.ok(employeeService.saveCandidateFirstRound(candidateID, candidateRequest));
    }
}
