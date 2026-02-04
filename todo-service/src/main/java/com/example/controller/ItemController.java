package com.example.controller;

import com.example.dto.request.ItemCreateDto;
import com.example.dto.request.ItemUpdateDto;
import com.example.dto.response.ItemResponseDto;
import com.example.dto.response.SimpleResponse;
import com.example.enums.TaskPriority;
import com.example.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/task")
@SecurityRequirement(name = "Token")
@Tag(name = "Todo", description = "Endpoints for managing todo tasks")
public class ItemController {

    private final ItemService itemService;

    @Operation(summary = "Create a new task", description = "Add a new todo task for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized or invalid token")
    })
    @PostMapping
    public ResponseEntity<SimpleResponse> addTask(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody ItemCreateDto itemCreateDto) {

        SimpleResponse response = new SimpleResponse();
        ItemResponseDto item = itemService.createItem(itemCreateDto, token);

        response.addMessage("message", "Task created successfully");
        response.addMessage("taskDetails", item);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update a task by ID", description = "Update an existing todo task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized or invalid token"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<SimpleResponse> updateTask(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @Valid @RequestBody ItemUpdateDto itemUpdateDto) {

        SimpleResponse response = new SimpleResponse();
        ItemResponseDto item = itemService.updateItem(id, itemUpdateDto, token);

        response.addMessage("message", "Task updated successfully");
        response.addMessage("taskDetails", item);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a task by ID", description = "Delete an existing todo task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized or invalid token"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<SimpleResponse> deleteTask(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {

        SimpleResponse response = new SimpleResponse();
        itemService.deleteItem(id, token);

        response.addMessage("message", "Task deleted successfully");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all tasks with pagination", description = "Retrieve all tasks of the authenticated user with paging")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks found successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized or invalid token")
    })
    @GetMapping
    public ResponseEntity<SimpleResponse> findAll(
            @RequestHeader("Authorization") String token,
            Pageable pageable) {

        SimpleResponse response = new SimpleResponse();
        Page<ItemResponseDto> items = itemService.findAll(pageable, token);

        response.addMessage("message", "Tasks found successfully");
        response.addMessage("items", items);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get a task by ID", description = "Retrieve a specific task by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task found successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized or invalid token"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping("/by-id/{id}")
    public ResponseEntity<SimpleResponse> findById(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {

        SimpleResponse response = new SimpleResponse();
        ItemResponseDto item = itemService.findById(id, token);

        response.addMessage("message", "Task found successfully");
        response.addMessage("taskDetails", item);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Search tasks by name", description = "Retrieve tasks that contain the given name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks found successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized or invalid token")
    })
    @GetMapping("/by-name/{name}")
    public ResponseEntity<SimpleResponse> findByName(
            @RequestHeader("Authorization") String token,
            @PathVariable String name,
            Pageable pageable) {

        SimpleResponse response = new SimpleResponse();
        Page<ItemResponseDto> items = itemService.findByName(name, pageable, token);

        response.addMessage("message", "Tasks found successfully");
        response.addMessage("items", items);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Search tasks by priority", description = "Retrieve tasks that have the specified priority")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks found successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized or invalid token")
    })
    @GetMapping("/by-taskPriority/{taskPriority}")
    public ResponseEntity<SimpleResponse> findByPriority(
            @RequestHeader("Authorization") String token,
            @PathVariable TaskPriority taskPriority,
            Pageable pageable) {

        SimpleResponse response = new SimpleResponse();
        Page<ItemResponseDto> items = itemService.findByPriority(taskPriority, pageable, token);

        response.addMessage("message", "Tasks found successfully");
        response.addMessage("items", items);

        return ResponseEntity.ok(response);
    }
}
