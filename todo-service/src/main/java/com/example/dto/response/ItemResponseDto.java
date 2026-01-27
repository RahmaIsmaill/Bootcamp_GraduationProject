package com.example.dto.response;

import com.example.enums.TaskPriority;
import com.example.enums.TaskStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ItemResponseDto {
    private Long itemId;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private TaskPriority taskPriority;
    private TaskStatus taskStatus;
}

