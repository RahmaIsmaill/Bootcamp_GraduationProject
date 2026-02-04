package com.example.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {
//    @NotBlank(message = "Name is required")
    @Size(max = 50, message = "Name can't exceed 50 characters")
    private String name;

    private MultipartFile coverImage;
}
