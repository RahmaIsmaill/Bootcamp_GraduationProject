package com.example.dto.request;


import com.example.enums.TaskPriority;
import com.example.enums.TaskStatus;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemUpdateDto {

    @Size(max = 80)
    private String title;

    @Size(max = 120)
    private String description;

    private TaskPriority taskPriority;

    private TaskStatus taskStatus;

    private Long userId; //get from token
}
