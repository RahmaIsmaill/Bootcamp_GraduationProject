package com.example.userservice.service;

import com.example.userservice.dto.request.UserCreateDto;
import com.example.userservice.dto.request.UserLoginDto;
import com.example.userservice.dto.request.UserResetPassword;
import com.example.userservice.dto.response.OtpGenerationResponse;
import com.example.userservice.dto.response.UserLoginResponse;
import com.example.userservice.dto.response.UserResponseDto;
import com.example.userservice.entity.User;
import jakarta.mail.MessagingException;

public interface AuthService {

    UserResponseDto register(UserCreateDto userCreateDto) throws MessagingException;
    UserLoginResponse login(UserLoginDto userLoginDto);
    void activateUser(String token, String otp) ;
    OtpGenerationResponse generateOtp(User user) throws MessagingException;
    OtpGenerationResponse reGenerateOtp(String email) throws MessagingException;
    OtpGenerationResponse forgetPassword(String token) throws MessagingException;
    void changePassword(String token, String otp, UserResetPassword userResetPassword);
    UserResponseDto checkToken(String token) ;
}
