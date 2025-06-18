package de.hsesslingen.focusflowbackend.repository;

import de.hsesslingen.focusflowbackend.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

// This interface provides access to team-related database operations
public interface TeamRepository extends JpaRepository<Team, Long> { 
}
