package com.example.userservice.controller;

import com.example.userservice.dto.request.UserCreateDto;
import com.example.userservice.dto.request.UserLoginDto;
import com.example.userservice.dto.request.UserResetPassword;
import com.example.userservice.dto.response.OtpGenerationResponse;
import com.example.userservice.dto.response.UserLoginResponse;
import com.example.userservice.dto.response.UserResponseDto;
import com.example.userservice.service.AuthService;
import com.example.userservice.service.JwtService;
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
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@Valid @RequestBody UserCreateDto dto) throws MessagingException {
        return ResponseEntity.ok(authService.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDto userLoginDto) {
        try {
            UserLoginResponse response = authService.login(userLoginDto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(error);
        }
    }

    @PostMapping("/activate")
    public ResponseEntity<String> activateUser(@RequestParam String email, @RequestParam String otp) {
        authService.activateUser(email, otp);
        return ResponseEntity.ok("User activated successfully");
    }

    @PostMapping("/regenerateOtp")
    public ResponseEntity<OtpGenerationResponse> regenerateOtp(@RequestParam String email) throws MessagingException {
        return ResponseEntity.ok(authService.reGenerateOtp(email));
    }

    @GetMapping("/checkToken")
    public ResponseEntity<?> checkToken(@RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        return jwtService.isValidToken(token.substring(7))
                ? ResponseEntity.ok("Token is valid")
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid");
    }

    @PostMapping("/forgetPassword")
    public ResponseEntity<OtpGenerationResponse> forgotPassword(@RequestHeader("Authorization") String token) throws MessagingException {
        OtpGenerationResponse otpGenerationResponse=authService.forgetPassword(token);
        return ResponseEntity.ok(otpGenerationResponse);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(
            @RequestHeader("Authorization") String token,
            @RequestParam String otp,
            @RequestBody UserResetPassword userResetPassword
    ) {
        authService.changePassword(token, otp, userResetPassword);
        return ResponseEntity.ok("Password changed successfully");
    }

}
