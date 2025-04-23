package de.hsesslingen.focusflowbackend.repository;

import de.hsesslingen.focusflowbackend.model.User;
import de.hsesslingen.focusflowbackend.model.Team;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
// This interface provides access to user-related database operations
public interface UserRepository extends JpaRepository<User, Long> {

    // Find a user by their email address
    Optional<User> findByEmail(String email);

    // Query users by role
    List<User> findByRole(String role);

    // Query users by team membership
    List<User> findByTeams(Team team);
}


