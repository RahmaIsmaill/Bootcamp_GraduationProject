package com.example.userservice.controller;

import com.example.userservice.dto.request.UserCreateDto;
import com.example.userservice.dto.request.UserLoginDto;
import com.example.userservice.dto.request.UserResetPassword;
import com.example.userservice.dto.response.OtpGenerationResponse;
import com.example.userservice.dto.response.UserLoginResponse;
import com.example.userservice.dto.response.UserResponseDto;
import com.example.userservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints related to user authentication, activation, and password management")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register a new user", description = "Registers a user and sends activation OTP to email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@Valid @RequestBody UserCreateDto dto) throws MessagingException {
        return ResponseEntity.ok(authService.register(dto));
    }

    @Operation(summary = "Login user", description = "Logs in a user and returns JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "400", description = "Invalid email or password")
    })
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDto userLoginDto) {
        try {
            UserLoginResponse response = authService.login(userLoginDto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @Operation(summary = "Activate user account", description = "Activates user account using email and OTP")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User activated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid OTP or email")
    })
    @PostMapping("/activate")
    public ResponseEntity<String> activateUser(@RequestParam String email, @RequestParam String otp) {
        authService.activateUser(email, otp);
        return ResponseEntity.ok("User activated successfully");
    }

    @Operation(summary = "Regenerate OTP", description = "Regenerates activation OTP and sends to email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OTP regenerated successfully"),
            @ApiResponse(responseCode = "400", description = "Email not found")
    })
    @PostMapping("/regenerateOtp")
    public ResponseEntity<OtpGenerationResponse> regenerateOtp(@RequestParam String email) throws MessagingException {
        return ResponseEntity.ok(authService.reGenerateOtp(email));
    }

    @Operation(summary = "Check JWT token", description = "Checks if the provided JWT token is valid")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token is valid"),
            @ApiResponse(responseCode = "401", description = "Token is invalid or missing")
    })
    @GetMapping("/checkToken")
    public ResponseEntity<Long> checkToken(@RequestHeader("Authorization") String token)  {
        UserResponseDto user=authService.checkToken(token);
        Long userId=user.getId();
        return ResponseEntity.ok(userId);
    }

    @Operation(summary = "Forgot password", description = "Generates OTP for password reset")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OTP generated and sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid token or user not found")
    })
    @PostMapping("/forgetPassword")
    public ResponseEntity<OtpGenerationResponse> forgotPassword(String email) throws MessagingException {
        OtpGenerationResponse otpGenerationResponse = authService.forgetPassword(email);
        return ResponseEntity.ok(otpGenerationResponse);
    }

    @Operation(summary = "Change password", description = "Changes user password using OTP")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid OTP or token")
    })
    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(
             String email,
            @RequestParam String otp,
            @RequestBody UserResetPassword userResetPassword
    ) {
        authService.changePassword(email, otp, userResetPassword);
        return ResponseEntity.ok("Password changed successfully");
    }

}
