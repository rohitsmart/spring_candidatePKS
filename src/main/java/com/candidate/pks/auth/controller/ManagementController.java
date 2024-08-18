package com.candidate.pks.auth.controller;

import com.candidate.pks.auth.dto.AddEmployeeRequest;
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
@Tag(name = "Management", description = "Operations related to Employee Management such as adding, updating, and deleting employees.")
public class ManagementController {

    private final ManagementService managementService;

    @PostMapping("save")
    @Operation(
            summary = "Add New Employee",
            description = "Adds a new employee to the system. This operation requires authentication and authorization."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee successfully added to the system.",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data. Please verify the provided employee details."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Access is denied due to invalid credentials or insufficient permissions."),
            @ApiResponse(responseCode = "500", description = "Internal server error. An unexpected error occurred while processing the request.")
    })
    public ResponseEntity<Response> addEmployee(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Details of the employee to be added, including name, position, and contact information.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AddEmployeeRequest.class))
            )
            @RequestBody AddEmployeeRequest addEmployeeRequest
    ) {
        Response response = managementService.addEmployee(addEmployeeRequest);
        return ResponseEntity.ok(response);
    }
}
