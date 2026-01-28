package com.example.userservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateDto {

    @Email(message = "Email should be valid")
    private String email;

    @Size(min = 6, max = 20 ,message = "password length is between 6 and 20")
    private String password;

    private Boolean enabled;
}
