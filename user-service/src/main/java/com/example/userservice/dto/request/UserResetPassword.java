package com.example.userservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(
        description = "Request object used to resetPassword"
)
public class UserResetPassword {

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 20 ,message = "password length is between 6 and 20")
    @Schema(description = "Password for the user", example = "test123")
    private String password;

    @NotBlank(message = "PasswordConfirmation is required")
    @Schema(description = "Confirm password must match password", example = "test123")
    private String passwordConfirm;
}
