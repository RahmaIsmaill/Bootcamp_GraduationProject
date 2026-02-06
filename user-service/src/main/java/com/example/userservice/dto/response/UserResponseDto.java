package com.example.userservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(
        description = "Response object that contains basic user information"
)
public class UserResponseDto {
    @Schema(
            description = "Unique identifier of the user",
            example = "1"
    )
    private Long id;
    @Schema(description = "User email", example = "test@gmail.com")
    private String email;
    @Schema(description = "User account status (true = active, false = disabled)",
            example = "true", defaultValue = "false")
    private Boolean enabled;
    @Schema(description = "One-time password sent to user email", example = "123456")
    private String otp;

}
