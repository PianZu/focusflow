package de.hsesslingen.focusflowbackend.service;

import org.springframework.stereotype.Service;
import de.hsesslingen.focusflowbackend.model.User;
import de.hsesslingen.focusflowbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
// This service class is responsible for handling user-related operations
public class UserService {

    private final UserRepository userRepository;

    // Method: Save the user to the database
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // Method: Find a user by their ID
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    // Method: Register a user with validation logic
    public User registerUser(User user) {
        if (!isValid(user)) {
            throw new IllegalArgumentException("Invalid user data");
        }
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    // Method: Authenticate a user with email and password
    public boolean login(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        return optionalUser.isPresent() && optionalUser.get().getPassword().equals(password);
    }

    // Method: Validate user credentials
    private boolean isValid(User user) {
        return user.getPassword() != null && user.getPassword().length() >= 8 &&
               user.getEmail() != null && user.getEmail().contains("@");
    }

    // Method: Assign a role to a user
    public User assignRole(User user, String role) {
        user.setRole(role);
        return userRepository.save(user);
}
}
