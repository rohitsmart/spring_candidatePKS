package com.candidate.pks.candidate.controller;

import com.candidate.pks.auth.dto.LoginResponse;
import com.candidate.pks.candidate.dto.AddCandidateRequest;
import com.candidate.pks.candidate.dto.InitialCommitRequest;
import com.candidate.pks.candidate.service.CandidateService;
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
@RequestMapping("/api/protected/candidate/")
@RequiredArgsConstructor
@Tag(name = "Candidate", description = "APIs related to Candidate Management")
public class CandidateController {

    private final CandidateService candidateService;

    // API need to be execute by Hr
    @PostMapping("save")
    @Operation(summary = "add  candidate", description = "Add Candidate ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Added Employee", content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Response> addCandidate(@RequestBody AddCandidateRequest addCandidateRequest) {
        Response response = candidateService.addCandidate(addCandidateRequest);
        return ResponseEntity.ok(response);
    }

    // API need to be execute by Hr
    @PostMapping("initial-commit")
    @Operation(summary = "initial Commit to candidate", description = " initialCommit ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Added Employee", content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Response> initialCommit(@RequestBody InitialCommitRequest initialCommitRequest) {
        Response response = candidateService.initialCommit(initialCommitRequest);
        return ResponseEntity.ok(response);
    }


}
