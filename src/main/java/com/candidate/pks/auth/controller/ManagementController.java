package com.candidate.pks.auth.controller;


import com.candidate.pks.auth.dto.AddEmployeeRequest;
import com.candidate.pks.auth.dto.LoginResponse;
import com.candidate.pks.auth.service.ManagementService;
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
@RequestMapping("/api/protected/management/")
@RequiredArgsConstructor
@Tag(name = "Management", description = "APIs related to Employee Management")
public class ManagementController {

    private final ManagementService managementService;

    @PostMapping("save")
    @Operation(summary = "add  employee", description = "Add Employee ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Added Employee", content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Response> addEmployee(@RequestBody AddEmployeeRequest addEmployeeRequest) {
        Response response = managementService.addEmployee(addEmployeeRequest);
        return ResponseEntity.ok(response);
    }
}
