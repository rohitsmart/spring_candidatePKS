package com.candidate.pks.auth.controller;

import com.candidate.pks.auth.dto.ForgetPasswordRequest;
import com.candidate.pks.auth.dto.LoginRequest;
import com.candidate.pks.auth.dto.LoginResponse;
import com.candidate.pks.auth.service.AuthService;
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
@RequestMapping("/api/public/")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Handles user authentication, including login and token management.")
public class AuthController {

    private final AuthService authService;

    @PostMapping("login")
    @Operation(
            summary = "Authenticate User",
            description = "Validates the user's credentials and provides an authentication token upon successful login.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User's login credentials, including username and password.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = LoginRequest.class))
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful. Returns authentication token and user details.",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input. Check the submitted login details."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. Invalid credentials or account is locked."),
            @ApiResponse(responseCode = "500", description = "Internal server error. Please try again later.")
    })
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
    @PostMapping("send-otp")
    @Operation(
            summary = "Forgot Password",
            description = "Allows users to initiate a password reset by providing their registered email address. An email with reset instructions will be sent if the email is associated with an account.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User's registered email address.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ForgetPasswordRequest.class))
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset email sent successfully.",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Email not found in the system."),
            @ApiResponse(responseCode = "500", description = "Internal server error. Please try again later.")
    })
    public ResponseEntity<Response> forgetPassword(@RequestBody ForgetPasswordRequest forgetPasswordRequest) {
        Response response = authService.forgetPassword(forgetPasswordRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("reset-password")
    @Operation(
            summary = "Forgot Password",
            description = "Allows users to initiate a password reset by providing their registered email address. An email with reset instructions will be sent if the email is associated with an account.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User's registered email address.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ForgetPasswordRequest.class))
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset email sent successfully.",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Email not found in the system."),
            @ApiResponse(responseCode = "500", description = "Internal server error. Please try again later.")
    })
    public ResponseEntity<Response> resetPassword(@RequestBody ForgetPasswordRequest forgetPasswordRequest) {
        Response response = authService.resetPassword(forgetPasswordRequest);
        return ResponseEntity.ok(response);
    }
}
