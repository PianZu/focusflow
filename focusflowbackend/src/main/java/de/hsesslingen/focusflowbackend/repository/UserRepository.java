package de.hsesslingen.focusflowbackend.repository;

import de.hsesslingen.focusflowbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // for login
    Optional<User> findByEmail(String email);

    // for upcoming tests (z. B. Team-members with certain ID)
    List<User> findByTeams_Id(Long teamId);


}

