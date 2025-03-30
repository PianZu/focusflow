package de.hsesslingen.focusflowbackend.service;

import org.springframework.stereotype.Service;
import de.hsesslingen.focusflowbackend.model.User;
import de.hsesslingen.focusflowbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import java.util.Optional;

@Service
@RequiredArgsConstructor
// This service class is responsible for handling user-related operations
public class UserService {

    private UserRepository userRepository;

    // Method: Save the user to the database
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // Method: Find a user by their ID
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }   
}
