package de.hsesslingen.focusflowbackend.repository;

import de.hsesslingen.focusflowbackend.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> { 
}
