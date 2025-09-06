package com.example.Bookstore.Controller;

import com.example.Bookstore.RequestBody.LoginRequest;
import com.example.Bookstore.RequestBody.LoginResponse;
import com.example.Bookstore.RequestBody.RegistrationBody;
import com.example.Bookstore.Service.UserService;
import com.example.Bookstore.Util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "User", description = "Register and login a user.")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public UserController(AuthenticationManager authenticationManager,
                          UserService userService,
                          JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    @Operation(summary = "Register user.")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegistrationBody registrationBody) {
        logger.info("Attempting to register a new user with username: {}", registrationBody.getUsername());
        String user = userService.registerUser(registrationBody);
        logger.info("User '{}' registered successfully.", registrationBody.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/login")
    @Operation(summary = "Login user.")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info("Login attempt for username: {}", loginRequest.getUsername());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        final UserDetails userDetails = userService.loadUserByUsername(loginRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        logger.info("User '{}' logged in successfully.", loginRequest.getUsername());
        return ResponseEntity.ok(new LoginResponse(jwt));
    }
}
