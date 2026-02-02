package com.example.userservice.controller;

import com.example.userservice.dto.request.UserCreateDto;
import com.example.userservice.dto.request.UserProfileDto;
import com.example.userservice.dto.request.UserUpdateDto;
import com.example.userservice.dto.response.UserProfileResponseDto;
import com.example.userservice.dto.response.UserResponseDto;
import com.example.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateDto dto) {
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/profile/{id}")
    public ResponseEntity<UserProfileResponseDto> editUserProfile(
            @PathVariable Long id,
            @ModelAttribute UserProfileDto dto) {
        return ResponseEntity.ok(userService.createUserProfile(id, dto));
    }
}
