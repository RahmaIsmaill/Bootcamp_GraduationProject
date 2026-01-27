package com.example.dto.request;

import com.example.enums.TaskPriority;
import com.example.enums.TaskStatus;
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
public class ItemCreateDto {

    @NotBlank(message = "title is required")
    @Size(max = 80)
    private String title;

    @NotBlank(message = "description is required")
    @Size(max = 120)
    private String description;


    @NotNull(message = "taskPriority is required")
    private TaskPriority taskPriority;

    @NotNull(message = "taskStatus is required")
    private TaskStatus taskStatus;

    private Long userId;
}
