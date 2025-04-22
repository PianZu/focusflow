package de.hsesslingen.focusflowbackend.repository;

import de.hsesslingen.focusflowbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// This interface provides access to user-related database operations
public interface UserRepository extends JpaRepository<User, Long> {

    // Method: Find a user by their email address
    Optional<User> findByEmail(String email);

    // Method: Find all users with a specific role
    List<User> findByRole(String role);

    // Method: Find all users who are members of a specific team
    List<User> findByTeams_Id(Long teamId);
}


