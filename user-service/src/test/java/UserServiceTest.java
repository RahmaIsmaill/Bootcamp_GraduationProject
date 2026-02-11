import com.example.userservice.dto.request.UserProfileDto;
import com.example.userservice.dto.request.UserUpdateDto;
import com.example.userservice.dto.response.UserProfileResponseDto;
import com.example.userservice.dto.response.UserResponseDto;
import com.example.userservice.entity.User;
import com.example.userservice.entity.UserProfile;
import com.example.userservice.repository.UserProfileRepository;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.JwtService;
import com.example.userservice.service.UserServiceImpl;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void updateUserTest() {
        User user = new User();
        user.setId(1L);
        user.setEmail("Rahma@gmail.com");
        user.setPassword(bCryptPasswordEncoder.encode("12345678"));
        user.setEnabled(true);

        UserUpdateDto dto = new UserUpdateDto();
        dto.setEmail("Rahmaismail@gmail.com");
        dto.setPassword("12345678");
        dto.setEnabled(false);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bCryptPasswordEncoder.encode("12345678")).thenReturn("encoded12345678");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDto updated = userService.updateUser(1L, dto);

        assertEquals("Rahmaismail@gmail.com", updated.getEmail());
        assertEquals(false, updated.getEnabled());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void deleteUserTest() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void createUserProfileTest() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");

        UserProfile profile = new UserProfile();
        profile.setUser(user);

        UserProfileDto dto = mock(UserProfileDto.class);
        MultipartFile file = mock(MultipartFile.class);
        when(dto.getName()).thenReturn("Rahmah");
        when(dto.getCoverImage()).thenReturn(file);
        when(file.isEmpty()).thenReturn(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userProfileRepository.findByUserId(1L)).thenReturn(Optional.empty());
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(profile);

        UserProfileResponseDto response = userService.createUserProfile(1L, dto);

        assertEquals("Rahmah", response.getName());
        verify(userProfileRepository, times(1)).save(any(UserProfile.class));
    }

    @Test
    void getCurrentUserTest() {
        String token = "test-token";
        Claims claims = mock(Claims.class);
        User user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");

        when(jwtService.parseJwtClaims(token)).thenReturn(claims);
        when(claims.getSubject()).thenReturn("test@gmail.com");
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(user));

        UserResponseDto response = userService.getCurrentUser(token);

        assertEquals("test@gmail.com", response.getEmail());
    }


}
