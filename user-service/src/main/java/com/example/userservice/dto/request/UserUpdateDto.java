package com.example.userservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(
        description = "Request object used to update user profile information"
)
public class UserUpdateDto {

    @Email(message = "Email should be valid")
    @Schema(description = "User email", example = "test@gmail.com")
    private String email;

    @Size(min = 6, max = 20 ,message = "password length is between 6 and 20")
    @Schema(description = "Password for the user", example = "test123")
    private String password;

    @Schema(description = "User account status (true = active, false = disabled)",
            example = "true", defaultValue = "false")
    private Boolean enabled;}
