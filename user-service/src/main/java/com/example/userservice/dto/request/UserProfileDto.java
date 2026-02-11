package com.example.userservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description = "Request object used to update user profile information"
)
public class UserProfileDto {
//    @NotBlank(message = "Name is required")
    @Size(max = 50, message = "Name can't exceed 50 characters")
    @Schema(description = "User full name", example = "Rahma Ismail")
    private String name;

    @Schema(description = "User cover image file", type = "file", format = "binary")
    private MultipartFile coverImage;
}
