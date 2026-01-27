package com.example.controller;

import com.example.dto.request.ItemCreateDto;
import com.example.dto.request.ItemUpdateDto;
import com.example.dto.response.ItemResponseDto;
import com.example.dto.response.SimpleResponse;
import com.example.enums.TaskPriority;
import com.example.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/task")
public class ItemController {

    private final ItemService itemService;

    // ================= CREATE =================
    @PostMapping
    public ResponseEntity<SimpleResponse> addTask(
            @Valid @RequestBody ItemCreateDto itemCreateDto) {

        SimpleResponse response = new SimpleResponse();
        ItemResponseDto item = itemService.createItem(itemCreateDto);

        response.addMessage("message", "Task created successfully");
        response.addMessage("taskDetails", item);

        return ResponseEntity.ok(response);
    }

    // ================= UPDATE =================
    @PatchMapping("/{id}")
    public ResponseEntity<SimpleResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody ItemUpdateDto itemUpdateDto) {

        SimpleResponse response = new SimpleResponse();
        ItemResponseDto item = itemService.updateItem(id, itemUpdateDto);

        response.addMessage("message", "Task updated successfully");
        response.addMessage("taskDetails", item);

        return ResponseEntity.ok(response);
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<SimpleResponse> deleteTask(@PathVariable Long id) {

        SimpleResponse response = new SimpleResponse();
        itemService.deleteItem(id);

        response.addMessage("message", "Task deleted successfully");
        return ResponseEntity.ok(response);
    }

    // ================= FIND ALL (PAGINATION) =================
    @GetMapping
    public ResponseEntity<SimpleResponse> findAll(Pageable pageable) {

        SimpleResponse response = new SimpleResponse();
        Page<ItemResponseDto> items = itemService.findAll(pageable);

        response.addMessage("message", "Items found successfully");
        response.addMessage("items", items);

        return ResponseEntity.ok(response);
    }

    // ================= FIND BY ID =================
    @GetMapping("/by-id/{id}")
    public ResponseEntity<SimpleResponse> findById(@PathVariable Long id) {

        SimpleResponse response = new SimpleResponse();
        ItemResponseDto item = itemService.findById(id);

        response.addMessage("message", "Item found successfully");
        response.addMessage("itemDetails", item);

        return ResponseEntity.ok(response);
    }

    // ================= FIND BY NAME (PAGINATION) =================
    @GetMapping("/by-name/{name}")
    public ResponseEntity<SimpleResponse> findByName(
            @PathVariable String name,
            Pageable pageable) {

        SimpleResponse response = new SimpleResponse();
        Page<ItemResponseDto> items = itemService.findByName(name, pageable);

        response.addMessage("message", "Items found successfully");
        response.addMessage("items", items);

        return ResponseEntity.ok(response);
    }

    // ================= FIND BY PRIORITY (PAGINATION) =================
    @GetMapping("/by-taskPriority/{taskPriority}")
    public ResponseEntity<SimpleResponse> findByPriority(
            @PathVariable TaskPriority taskPriority,
            Pageable pageable) {

        SimpleResponse response = new SimpleResponse();
        Page<ItemResponseDto> items = itemService.findByPriority(taskPriority, pageable);

        response.addMessage("message", "Items found successfully");
        response.addMessage("items", items);

        return ResponseEntity.ok(response);
    }
}
