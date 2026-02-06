package com.example.userservice.service;

import com.example.userservice.dto.request.UserCreateDto;
import com.example.userservice.dto.request.UserLoginDto;
import com.example.userservice.dto.request.UserResetPassword;
import com.example.userservice.dto.response.OtpGenerationResponse;
import com.example.userservice.dto.response.UserLoginResponse;
import com.example.userservice.dto.response.UserResponseDto;
import com.example.userservice.entity.Otp;
import com.example.userservice.entity.User;
import com.example.userservice.exception.GlobalException;
import com.example.userservice.repository.OtpRepository;
import com.example.userservice.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final OtpRepository otpRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Override
    public UserResponseDto register(UserCreateDto userCreateDto) throws MessagingException {

        if (userRepository.existsByEmail(userCreateDto.getEmail())) {
            throw new GlobalException(Map.of("Error", "Email already exists"));
        }

        if (!userCreateDto.getPassword().equals(userCreateDto.getPasswordConfirm())) {
            throw new GlobalException(Map.of("Error", "Passwords don't match"));
        }
        User user = User.builder()
                .email(userCreateDto.getEmail())
                .password(bCryptPasswordEncoder.encode(userCreateDto.getPassword()))
                .enabled(false)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        OtpGenerationResponse otpGenerationResponse = reGenerateOtp(user.getEmail());

        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .enabled(user.isEnabled())
                .otp(otpGenerationResponse.getOtp())
                .build();
    }

    @Override
    public UserLoginResponse login(UserLoginDto userLoginDto) {

        User user = userRepository.findByEmail(userLoginDto.getEmail())
                .orElseThrow(() -> new GlobalException(Map.of("Error", "User not found")));

        if (!user.isEnabled()) {
            throw new GlobalException(Map.of("Error", "User not activated. Please verify OTP."));
        }
        System.out.println("RAW: " + userLoginDto.getPassword());
        System.out.println("HASH: " + user.getPassword());
        System.out.println(
                bCryptPasswordEncoder.matches(userLoginDto.getPassword(), user.getPassword())
        );


        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLoginDto.getEmail(),
                            userLoginDto.getPassword()
                    )
            );

        } catch (BadCredentialsException e) {
            throw new GlobalException(Map.of("Error", "Invalid email or password"));
        }

        String token = jwtService.generateToken(user, new HashMap<>());
        jwtService.saveUserToken(user, token);

        return UserLoginResponse.builder()
                .email(user.getEmail())
                .token(token)
                .build();
    }

    public void activateUser(String email, String otp) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GlobalException(Map.of("Error", "User not found")));

        Otp userOtp = otpRepository.findByUser(user)
                .orElseThrow(() -> new GlobalException(Map.of("Error", "OTP not found")));

        if (userOtp.getExpirationTime().isBefore(LocalDateTime.now())) {
            throw new GlobalException(Map.of("Error", "OTP expired"));
        }

        if (userOtp.getOtp().equals(otp)) {
            user.setEnabled(true);
            userRepository.save(user);
            otpRepository.delete(userOtp);
        } else {
            throw new GlobalException(Map.of("Error", "Invalid OTP"));
        }
    }


    public OtpGenerationResponse reGenerateOtp(String email) throws MessagingException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GlobalException(Map.of("Error", "User not found")));

        List<Otp> oldOtps = otpRepository.findAllByUser(user);
        if (!oldOtps.isEmpty()) {
            otpRepository.deleteAll(oldOtps);
        }

        String otpValue = String.valueOf((int) (Math.random() * 900000) + 100000);
           Otp newOtp = Otp.builder()
                    .otp(otpValue)
                    .expirationTime(LocalDateTime.now().plusMinutes(2))
                    .user(user)
                    .build();
            otpRepository.save(newOtp);

        emailService.sendOtp(user.getEmail(), newOtp.getOtp());

        return OtpGenerationResponse.builder()
                .expirationTime(newOtp.getExpirationTime())
                .otp(newOtp.getOtp())
                .build();
    }

    public OtpGenerationResponse forgetPassword(String email) throws MessagingException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GlobalException(Map.of("Error", "User not found")));

        return reGenerateOtp(user.getEmail());
    }


    public void changePassword(String email, String otp, UserResetPassword userResetPassword) {


        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GlobalException(Map.of("Error", "User not found")));

        Otp otp1 = otpRepository.findByUser(user)
                .orElseThrow(() -> new GlobalException(Map.of("Error", "Otp not found")));

        if (otp1.getExpirationTime().isBefore(LocalDateTime.now())) {
            throw new GlobalException(Map.of("Error", "OTP expired"));
        }

        if (!otp1.getOtp().equals(otp)) {
            throw new GlobalException(Map.of("Error", "Invalid OTP"));
        }

        if (!userResetPassword.getPassword()
                .equals(userResetPassword.getPasswordConfirm())) {
            throw new GlobalException(Map.of("Error", "Passwords do not match"));
        }

        user.setPassword(
                bCryptPasswordEncoder.encode(userResetPassword.getPassword())
        );
        userRepository.save(user);

        otpRepository.delete(otp1); //One-Time Password , available for one use
    }

    @Override
    public UserResponseDto checkToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new GlobalException(Map.of("token", "Invalid token"));
        }
        if (!jwtService.isValidToken(token.substring(7).trim())) {
            throw new GlobalException(Map.of("token", "Token is invalid or expired"));

        }

        return userService.getCurrentUser(token.substring(7));
    }


}
