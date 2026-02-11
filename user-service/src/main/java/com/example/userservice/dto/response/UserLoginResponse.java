package com.example.userservice.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
        description = "Response object that contains login response"
)
public class UserLoginResponse {

    @Schema(description = "User email", example = "test@gmail.com")
    private String email;

    @Schema(description = "JWT token used for authentication",
            example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIn0.signature")
    private String token;

}
