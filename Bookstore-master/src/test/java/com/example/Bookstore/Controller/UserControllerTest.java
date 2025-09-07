package com.example.Bookstore.Controller;

import com.example.Bookstore.RequestBody.LoginRequest;
import com.example.Bookstore.RequestBody.LoginResponse;
import com.example.Bookstore.RequestBody.RegistrationBody;
import com.example.Bookstore.Service.UserService;
import com.example.Bookstore.Util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private UserController userController;

    private RegistrationBody validRegistrationBody;
    private LoginRequest validLoginRequest;

    @BeforeEach
    void setUp() {
        validRegistrationBody = new RegistrationBody();
        validRegistrationBody.setUsername("testuser");
        validRegistrationBody.setPassword("password123");
        validRegistrationBody.setEmail("test@example.com");

        validLoginRequest = new LoginRequest();
        validLoginRequest.setUsername("testuser");
        validLoginRequest.setPassword("password123");
    }

    @Test
    void registerUser_ShouldReturnCreated_WhenRegistrationSuccessful() {
        when(userService.registerUser(validRegistrationBody)).thenReturn("Registration successful!");

        ResponseEntity<String> response = userController.registerUser(validRegistrationBody);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo("Registration successful!");
        verify(userService).registerUser(validRegistrationBody);
    }

    @Test
    void registerUser_ShouldPropagateException_WhenRegistrationFails() {
        when(userService.registerUser(validRegistrationBody))
                .thenThrow(new IllegalArgumentException("Email is already in use!"));

        assertThatThrownBy(() -> userController.registerUser(validRegistrationBody))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email is already in use!");
        
        verify(userService).registerUser(validRegistrationBody);
    }

    @Test
    void login_ShouldReturnJwtToken_WhenAuthenticationSuccessful() {

        String expectedJwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails)).thenReturn(expectedJwt);

        ResponseEntity<LoginResponse> response = userController.login(validLoginRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getToken()).isEqualTo(expectedJwt);
        
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService).loadUserByUsername("testuser");
        verify(jwtUtil).generateToken(userDetails);
    }

    @Test
    void login_ShouldPropagateException_WhenAuthenticationFails() {

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new org.springframework.security.core.AuthenticationException("Invalid credentials") {});

        assertThatThrownBy(() -> userController.login(validLoginRequest))
                .isInstanceOf(org.springframework.security.core.AuthenticationException.class)
                .hasMessage("Invalid credentials");
        
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService, never()).loadUserByUsername(anyString());
        verify(jwtUtil, never()).generateToken(any(UserDetails.class));
    }

    @Test
    void login_ShouldPropagateException_WhenUserNotFound() {

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userService.loadUserByUsername("testuser"))
                .thenThrow(new org.springframework.security.core.userdetails.UsernameNotFoundException("User not found"));

        assertThatThrownBy(() -> userController.login(validLoginRequest))
                .isInstanceOf(org.springframework.security.core.userdetails.UsernameNotFoundException.class)
                .hasMessage("User not found");
        
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService).loadUserByUsername("testuser");
        verify(jwtUtil, never()).generateToken(any(UserDetails.class));
    }

}
