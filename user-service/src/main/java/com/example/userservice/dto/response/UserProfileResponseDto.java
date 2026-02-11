package com.example.userservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description = "Response object that contains user profile information(name , photo)"
)
public class UserProfileResponseDto {
    @Schema(description = "User full name", example = "Rahma Ismail")
    private String name;
    @Schema(description = "User cover image file", type = "string")
    private String coverImageUrl;
}
