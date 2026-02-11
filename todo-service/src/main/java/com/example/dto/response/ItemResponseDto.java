package com.example.dto.response;

import com.example.enums.TaskPriority;
import com.example.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "DTO for creating a new ToDo task Response")
public class ItemResponseDto {
    @Schema(description = "Unique Id for the task", example = "1")
    private Long itemId;
    @Schema(description = "Title of the task", example = "test")
    private String title;
    @Schema(description = "Description of the task", example = "teestt")
    private String description;
    private LocalDateTime createdAt;
    @Schema(description = "Priority of the task", example = "HIGH")
    private TaskPriority taskPriority;
    @Schema(description = "Current status of the task", example = "PENDING")
    private TaskStatus taskStatus;
}

