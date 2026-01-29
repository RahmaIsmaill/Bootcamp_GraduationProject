package com.example.userservice.service;

import com.example.userservice.dto.request.UserCreateDto;
import com.example.userservice.dto.request.UserProfileDto;
import com.example.userservice.dto.request.UserUpdateDto;
import com.example.userservice.dto.response.UserProfileResponseDto;
import com.example.userservice.dto.response.UserResponseDto;
import com.example.userservice.entity.User;
import com.example.userservice.entity.UserProfile;
import com.example.userservice.repository.UserProfileRepository;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    @Override
    public UserResponseDto register(UserCreateDto userCreateDto) {

        if(userRepository.existsByEmail(userCreateDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if(!userCreateDto.getPassword().equals(userCreateDto.getPasswordConfirm())){
            throw new IllegalArgumentException("Passwords don't match");
        }
        User user = User.builder()
                .email(userCreateDto.getEmail())
                .password(userCreateDto.getPassword())
                .enabled(true)
                .build();

        userRepository.save(user);

        return mapToResponse(user);
    }

    @Override
    public UserResponseDto updateUser(Long id,UserUpdateDto dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }

        if (dto.getPassword() != null) {
            user.setPassword(dto.getPassword());
        }

        if (dto.getEnabled() != null) {
            user.setEnabled(dto.getEnabled());
        }

        userRepository.save(user);
        return mapToResponse(user);
    }
    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.delete(user);
    }

    @Override
    public UserProfileResponseDto createUserProfile(Long id,UserProfileDto userProfileDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfile profile = userProfileRepository
                .findByUserId(id)
                .orElseGet(() -> {
                    UserProfile p = new UserProfile();
                    p.setUser(user);
                    p.setName(user.getEmail());
                    return p;
                });

        if (userProfileDto.getName() != null && !userProfileDto.getName().isBlank()) {
            profile.setName(userProfileDto.getName());
        }

        if (userProfileDto.getCoverImage() != null && !userProfileDto.getCoverImage().isEmpty()) {
            String imageUrl = saveImage(userProfileDto.getCoverImage());
            profile.setCoverImageUrl(imageUrl);
        }

        userProfileRepository.save(profile);

        return new UserProfileResponseDto(
                profile.getName(),
                profile.getCoverImageUrl()
        );
    }

    private UserResponseDto mapToResponse(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .enabled(user.isEnabled())
                .build();
    }
    private String saveImage(MultipartFile file) {
        try {
            String uploadDir = "uploads/";
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDir + fileName);

            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());

            return "/uploads/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image");
        }
    }

}
