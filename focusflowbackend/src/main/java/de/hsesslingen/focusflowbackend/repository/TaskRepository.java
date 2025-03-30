package de.hsesslingen.focusflowbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.hsesslingen.focusflowbackend.model.tasks.Task;

public interface TaskRepository extends JpaRepository<Task, Long> { 
}
