package com.example.userservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@Schema(
        description = "Response object that contains otp information"
)
public class OtpGenerationResponse {
    @Schema(description = "OTP expiration timestamp", example = "2026-02-06T23:59:59")
    private LocalDateTime expirationTime;

    @Schema(description = "One-time password sent to user email", example = "123456")
    private String otp;
}
