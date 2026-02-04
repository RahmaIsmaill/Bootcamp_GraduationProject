package com.example.userservice.service;

import com.example.userservice.dto.request.UserCreateDto;
import com.example.userservice.dto.request.UserProfileDto;
import com.example.userservice.dto.request.UserUpdateDto;
import com.example.userservice.dto.response.UserProfileResponseDto;
import com.example.userservice.dto.response.UserResponseDto;

public interface UserService {

    UserResponseDto updateUser(Long id,UserUpdateDto userUpdateDto);
    void deleteUser(Long id);
    UserProfileResponseDto createUserProfile(Long id,UserProfileDto userProfileDto);
    UserResponseDto getCurrentUser(String jwtToken);

}
