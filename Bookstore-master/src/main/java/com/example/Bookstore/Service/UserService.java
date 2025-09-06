package com.example.Bookstore.Service;

import com.example.Bookstore.Constants.UserRole;
import com.example.Bookstore.RequestBody.RegistrationBody;
import com.example.Bookstore.Model.User;
import com.example.Bookstore.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String registerUser(RegistrationBody registrationBody) {
        Optional<User> existingEmail = userRepository.findByEmail(registrationBody.getEmail());
        Optional<User> existingUsername = userRepository.findByUsername(registrationBody.getUsername());

        if (existingEmail.isPresent()) {
            throw new IllegalArgumentException("Email is already in use!");
        }
        if (existingUsername.isPresent()) {
            throw new IllegalArgumentException("Username is already in use!");
        }

        User user = new User();
        user.setUsername(registrationBody.getUsername());
        user.setEmail(registrationBody.getEmail());
        user.setPassword(passwordEncoder.encode(registrationBody.getPassword()));
        user.setRole(UserRole.CLIENT);

        userRepository.save(user);
        return "Registration successful!";
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

//    public String loginUser(LoginBody loginBody, HttpSession session) {
//        Optional<User> optionalUser = userRepository.findByUsername(loginBody.getUsername());
//
//        if (optionalUser.isPresent()) {
//            User user = optionalUser.get();
//
//            if (passwordEncoder.matches(loginBody.getPassword(), user.getPassword())) {
//                String role = (loginBody.getRole() != null) ? loginBody.getRole() : "Client";
//                session.setAttribute("userId", user.getId());
//                session.setAttribute("role", loginBody.getRole()); // still vulnerable if you want it to be
//                return "Login successful!";
//            } else {
//                throw new IllegalArgumentException("Invalid password.");
//            }
//        } else {
//            throw new IllegalArgumentException("User not found.");
//        }
//   }
}



