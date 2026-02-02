package com.example.userservice.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OtpGenerationResponse {
    private LocalDateTime expirationTime;
    private String otp;
}
