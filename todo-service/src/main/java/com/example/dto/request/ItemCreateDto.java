package com.example.dto.request;

import com.example.enums.TaskPriority;
import com.example.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for creating a new ToDo task")
public class ItemCreateDto {

    @NotBlank(message = "title is required")
    @Size(max = 80)
    @Schema(description = "Title of the task", example = "test", maxLength = 80, required = true)
    private String title;

    @NotBlank(message = "description is required")
    @Size(max = 120)
    @Schema(description = "Description of the task", example = "teestt", maxLength = 120, required = true)
    private String description;

    @NotNull(message = "taskPriority is required")
    @Schema(description = "Priority of the task", example = "HIGH")
    private TaskPriority taskPriority;

    @NotNull(message = "taskStatus is required")
    @Schema(description = "Current status of the task", example = "PENDING")
    private TaskStatus taskStatus;

}
