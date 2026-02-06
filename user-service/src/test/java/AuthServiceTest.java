import com.example.userservice.dto.request.UserCreateDto;
import com.example.userservice.dto.request.UserLoginDto;
import com.example.userservice.dto.response.UserLoginResponse;
import com.example.userservice.dto.response.UserResponseDto;
import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.repository.OtpRepository;
import com.example.userservice.service.AuthServiceImpl;
import com.example.userservice.service.EmailService;
import com.example.userservice.service.JwtService;
import com.example.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private OtpRepository otpRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private EmailService emailService;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserService userService;
    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    public void registerTest() throws MessagingException {

        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setEmail("rahma@gmail.com");
        userCreateDto.setPassword("password");
        userCreateDto.setPasswordConfirm("password");

        when(userRepository.existsByEmail(userCreateDto.getEmail())).thenReturn(false);
        when(bCryptPasswordEncoder.encode(userCreateDto.getPassword())).thenReturn("encoded-password");

        User user = User.builder()
                .id(1L)
                .email(userCreateDto.getEmail())
                .password("encoded-password")
                .enabled(false)
                .createdAt(LocalDateTime.now())
                .build();

        when(userRepository.save(any(User.class))).thenReturn(user);
        doNothing().when(emailService).sendOtp(anyString(),anyString());

        UserResponseDto userResponseDto = authService.register(userCreateDto);
        assertEquals(userCreateDto.getEmail(), userResponseDto.getEmail());
        assertFalse(userResponseDto.getEnabled());
        assertNotNull(userResponseDto.getOtp());

        verify(userRepository, times(1)).save(any(User.class));
        verify(emailService, times(1)).sendOtp(eq(userCreateDto.getEmail()), anyString());

    }

    @Test
    public void loginTest() throws MessagingException {
        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setEmail("rahma@gmail.com");
        userLoginDto.setPassword("password");

        User user = User.builder()
                .id(1L)
                .email("rahma@gmail.com")
                .password("encoded-password")
                .enabled(true)
                .build();

        when(userRepository.findByEmail(userLoginDto.getEmail())).thenReturn(Optional.of(user));
        when(bCryptPasswordEncoder.matches(userLoginDto.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(eq(user), any(Map.class))).thenReturn("mocked-jwt-token");
        doNothing().when(jwtService).saveUserToken(user, "mocked-jwt-token");

        when((authenticationManager).authenticate(any()))
                .thenReturn(mock(Authentication.class));

        UserLoginResponse response = authService.login(userLoginDto);

        assertEquals("rahma@gmail.com", response.getEmail());
        assertEquals("mocked-jwt-token", response.getToken());

        verify(userRepository, times(1)).findByEmail(userLoginDto.getEmail());
        verify(authenticationManager, times(1)).authenticate(any());
        verify(jwtService, times(1)).generateToken(eq(user), any(Map.class));
        verify(jwtService, times(1)).saveUserToken(user, "mocked-jwt-token");

    }

}
