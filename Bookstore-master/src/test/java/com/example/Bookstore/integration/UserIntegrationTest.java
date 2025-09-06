package com.example.Bookstore.integration;

import com.example.Bookstore.RequestBody.RegistrationBody;
import com.example.Bookstore.RequestBody.LoginRequest;
import com.example.Bookstore.RequestBody.LoginResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
class UserIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldRegisterUserSuccessfully() {

        RegistrationBody registrationBody = new RegistrationBody();
        registrationBody.setUsername("testuser");
        registrationBody.setPassword("password123");
        registrationBody.setEmail("test@example.com");


        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/auth/register",
                registrationBody,
                String.class
        );


        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).contains("Registration successful!");
    }

    @Test
    void shouldLoginUserSuccessfully() {

        RegistrationBody registrationBody = new RegistrationBody();
        registrationBody.setUsername("loginuser");
        registrationBody.setPassword("password123");
        registrationBody.setEmail("login@example.com");

        restTemplate.postForEntity("/api/auth/register", registrationBody, String.class);


        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("loginuser");
        loginRequest.setPassword("password123");


        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(
                "/api/auth/login",
                loginRequest,
                LoginResponse.class
        );


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getToken()).isNotNull();
        assertThat(response.getBody().getToken()).isNotEmpty();
    }

    @Test
    void shouldRejectDuplicateUsername() {

        RegistrationBody firstUser = new RegistrationBody();
        firstUser.setUsername("duplicate");
        firstUser.setPassword("password123");
        firstUser.setEmail("first@example.com");

        RegistrationBody secondUser = new RegistrationBody();
        secondUser.setUsername("duplicate");  // Same username
        secondUser.setPassword("password456");
        secondUser.setEmail("second@example.com");

        ResponseEntity<String> firstResponse= restTemplate.postForEntity(
                "/api/auth/register",
                firstUser,
                String.class
        );
        ResponseEntity<String> secondResponse= restTemplate.postForEntity(
                "/api/auth/register",
                secondUser,
                String.class
        );


        assertThat(firstResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(secondResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldRejectInvalidCredentials() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("nonexistent");
        loginRequest.setPassword("password123");

        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/auth/login",
                loginRequest,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).contains("Invalid username or password");
    }


    @Test
    void shouldRejectInvalidRegistrationData() {
        RegistrationBody invalidRegistration = new RegistrationBody();
        invalidRegistration.setUsername("testuser");
        invalidRegistration.setPassword("password123");
        invalidRegistration.setEmail("invalid-email");

        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/auth/register",
                invalidRegistration,
                String.class
        );


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldRejectShortPassword() {

        RegistrationBody invalidRegistration = new RegistrationBody();
        invalidRegistration.setUsername("testuser");
        invalidRegistration.setPassword("123");
        invalidRegistration.setEmail("test@example.com");

        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/auth/register",
                invalidRegistration,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}