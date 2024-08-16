package com.candidate.pks.auth.controller;

import com.candidate.pks.auth.dto.LoginRequest;
import com.candidate.pks.auth.dto.LoginResponse;
import com.candidate.pks.auth.service.AuthService;
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
@Tag(name = "Authentication", description = "APIs related to user authentication and registration")
public class AuthController {

    private final AuthService authService;

    @PostMapping("login")
    @Operation(summary = "User Login", description = "Authenticate user and return a token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful login", content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }


}
