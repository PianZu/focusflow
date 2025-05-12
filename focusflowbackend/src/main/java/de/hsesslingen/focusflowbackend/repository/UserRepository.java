package de.hsesslingen.focusflowbackend.repository;

import de.hsesslingen.focusflowbackend.model.User;
import de.hsesslingen.focusflowbackend.model.Team;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
// This interface provides access to user-related database operations
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    List<User> findByRole(String role);
    List<User> findByTeams(Team team);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.teams WHERE u.id = :id")
    Optional<User> findByIdWithTeams(@Param("id") Long id);
}


