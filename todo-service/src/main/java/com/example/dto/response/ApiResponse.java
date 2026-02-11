package com.example.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Generic API response wrapper")
public class ApiResponse {

    @Schema(description = "Map containing response messages or data",
            example = "{\"success\": true, \"taskTitle\": \"test\"}")
    private Map<String, Object> Messages = new HashMap<>();
    public void addMessage(String key, Object value){
        Messages.put(key, value);
    }
}
