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
@Tag(name = "Employee", description = "APIs related to Employee Management")
public class EmployeeController {


    private final EmployeeService employeeService;

    @GetMapping("fetch-candidate")
    @Operation(summary = "Fetch candidates and their scheduled interviews", description = "Fetch candidates based on interview date range and pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful fetch of candidates and interviews", content = @Content(schema = @Schema(implementation = ListOfCandidate.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ListOfCandidate> fetchCandidate(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(employeeService.fetchCandidates(fromDate, toDate, page, size));
    }

    // face to face records filled by Employee for then candidate

    @PostMapping("candidate-first-round")
    @Operation(summary = "Fetch candidates and their scheduled interviews", description = "Fetch candidates based on interview date range and pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful fetch of candidates and interviews", content = @Content(schema = @Schema(implementation = ListOfCandidate.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Response> saveCandidateFirstRound(
            @RequestParam("candidateId") String candidateID ,
            @RequestBody CandidateRequest candidateRequest){
        return ResponseEntity.ok(employeeService.saveCandidateFirstRound(candidateID,candidateRequest));
    }
}
