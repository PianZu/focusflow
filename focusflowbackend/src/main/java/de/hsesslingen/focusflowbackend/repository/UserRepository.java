package de.hsesslingen.focusflowbackend.repository;

import de.hsesslingen.focusflowbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
// This interface provides access to user-related database operations
public interface UserRepository extends JpaRepository<User, Long> {
}


