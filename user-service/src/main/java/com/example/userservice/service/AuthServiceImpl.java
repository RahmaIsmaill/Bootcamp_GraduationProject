package com.example.userservice.service;

import com.example.userservice.dto.request.UserLoginDto;
import com.example.userservice.dto.response.UserLoginResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public UserLoginResponse login(UserLoginDto userLoginDto) {
        return null;
    }
}
