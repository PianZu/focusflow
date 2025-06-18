package de.hsesslingen.focusflowbackend.repository;

import de.hsesslingen.focusflowbackend.model.tasks.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
// This interface provides access to task-related database operations
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByAssigneeId(Long assigneeId);
    List<Task> findByTeamId(Long teamId);
}