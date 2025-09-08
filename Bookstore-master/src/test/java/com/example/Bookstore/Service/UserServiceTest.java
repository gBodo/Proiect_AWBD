package com.example.Bookstore.Service;

import com.example.Bookstore.Constants.UserRole;
import com.example.Bookstore.Model.User;
import com.example.Bookstore.Repository.UserRepository;
import com.example.Bookstore.RequestBody.RegistrationBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private RegistrationBody validRegistrationBody;
    private User testUser;

    @BeforeEach
    void setUp() {
        validRegistrationBody = new RegistrationBody();
        validRegistrationBody.setUsername("testuser");
        validRegistrationBody.setPassword("password123");
        validRegistrationBody.setEmail("test@example.com");

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setRole(UserRole.CLIENT);
    }

    @Test
    void registerUser_ShouldRegisterUserSuccessfully_WhenValidData() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        String result = userService.registerUser(validRegistrationBody);

        assertThat(result).isEqualTo("Registration successful!");
        verify(userRepository).findByEmail("test@example.com");
        verify(userRepository).findByUsername("testuser");
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_ShouldThrowException_WhenEmailAlreadyExists() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        assertThatThrownBy(() -> userService.registerUser(validRegistrationBody))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email is already in use!");
        
        verify(userRepository).findByEmail("test@example.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerUser_ShouldThrowException_WhenUsernameAlreadyExists() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        assertThatThrownBy(() -> userService.registerUser(validRegistrationBody))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Username is already in use!");
        
        verify(userRepository).findByEmail("test@example.com");
        verify(userRepository).findByUsername("testuser");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerUser_ShouldSetCorrectUserRole() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            assertThat(user.getRole()).isEqualTo(UserRole.CLIENT);
            return user;
        });

        userService.registerUser(validRegistrationBody);

        verify(userRepository).save(any(User.class));
    }

    @Test
    void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists() {

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        UserDetails result = userService.loadUserByUsername("testuser");

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("testuser");
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void loadUserByUsername_ShouldThrowException_WhenUserNotFound() {

        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.loadUserByUsername("nonexistent"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found: nonexistent");
        
        verify(userRepository).findByUsername("nonexistent");
    }

    @Test
    void registerUser_ShouldEncodePassword() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            assertThat(user.getPassword()).isEqualTo("encodedPassword");
            return user;
        });

        userService.registerUser(validRegistrationBody);

        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }
}
