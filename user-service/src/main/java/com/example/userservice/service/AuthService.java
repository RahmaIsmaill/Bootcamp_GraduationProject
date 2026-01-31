package com.example.userservice.service;

import com.example.userservice.dto.request.UserLoginDto;
import com.example.userservice.dto.response.UserLoginResponse;

public interface AuthService {

    UserLoginResponse login(UserLoginDto userLoginDto);
}
