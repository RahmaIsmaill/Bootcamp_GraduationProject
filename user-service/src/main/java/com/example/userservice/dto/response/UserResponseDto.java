package com.example.userservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDto {

    private Long id;
    private String email;
    private Boolean enabled;

}
