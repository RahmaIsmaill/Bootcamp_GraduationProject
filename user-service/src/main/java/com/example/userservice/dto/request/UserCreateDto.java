package com.example.userservice.dto.request;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserCreateDto {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 20 ,message = "password length is between 6 and 20")
    private String password;

    @NotBlank(message = "PasswordConfirmation is required")
    private String passwordConfirm;
}
