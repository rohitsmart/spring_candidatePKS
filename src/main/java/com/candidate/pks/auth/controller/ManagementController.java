package com.candidate.pks.auth.controller;

import com.candidate.pks.auth.dto.AddEmployeeRequest;
import com.candidate.pks.auth.dto.EmployeeResponseDTO;
import com.candidate.pks.auth.dto.UserEmployeeResponse;
import com.candidate.pks.auth.dto.UserEmployeeResponseList;
import com.candidate.pks.auth.model.User;
import com.candidate.pks.auth.service.ManagementService;
import com.candidate.pks.repeat.Response;
import com.candidate.pks.security.UserDetail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/protected/management/")
@RequiredArgsConstructor
@Tag(name = "Management", description = "Operations related to Employee Management such as adding, updating, and deleting employees.")
@SecurityRequirement(name = "bearerAuth")
public class ManagementController {

    private final ManagementService managementService;
    private final UserDetail userDetail;



    @GetMapping("/employees")
    @Operation(
            summary = "Get Employees",
            description = "Retrieves a paginated list of employees with their details."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the employee list."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public Page<EmployeeResponseDTO> getEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        return managementService.getEmployees(page, size);
    }

    @GetMapping("user-employee-details")
    @Operation(
            summary = "Get User and Employee Details",
            description = "Returns details of the user and the associated employee. This operation requires authentication and authorization."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user and employee details.",
                    content = @Content(schema = @Schema(implementation = UserEmployeeResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Access is denied due to invalid credentials or insufficient permissions."),
            @ApiResponse(responseCode = "500", description = "Internal server error. An unexpected error occurred while processing the request.")
    })
    public ResponseEntity<UserEmployeeResponse> getUserEmployeeDetails() {
        User user = userDetail.getUser();
        UserEmployeeResponse response = UserEmployeeResponse.builder()
                .username(user.getUsername())
                .firstName(user.getEmployee() != null ? user.getEmployee().getFirstName() : null)
                .lastName(user.getEmployee() != null ? user.getEmployee().getLastName() : null)
                .empId(user.getEmployee() != null ? user.getEmployee().getEmpId() : null)
                .designation(user.getEmployee() != null ? user.getEmployee().getDesignation() : null) // Ensure this is a String
                .role(user.getRoles().stream().findFirst().orElse(null)) // Use getRoles() method
                .build();

        return ResponseEntity.ok(response);
    }



    @GetMapping("emp-data")
    @Operation(
            summary = "Get User and Employee Details",
            description = "Returns details of the user and the associated employee. This operation requires authentication and authorization. You can search employees by providing a search string."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user and employee details.",
                    content = @Content(schema = @Schema(implementation = UserEmployeeResponseList.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Access is denied due to invalid credentials or insufficient permissions."),
            @ApiResponse(responseCode = "500", description = "Internal server error. An unexpected error occurred while processing the request.")
    })
    public ResponseEntity<UserEmployeeResponseList> getUserEmployeeData(
            @RequestParam(required = false, defaultValue = "") String search) {
        return ResponseEntity.ok(managementService.getUserEmployeeData(search));
    }

}
