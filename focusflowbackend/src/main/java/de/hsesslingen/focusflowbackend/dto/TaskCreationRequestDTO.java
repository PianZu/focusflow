package de.hsesslingen.focusflowbackend.dto;

import de.hsesslingen.focusflowbackend.model.tasks.TaskPriority;
import de.hsesslingen.focusflowbackend.model.tasks.TaskStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
/**
 * TaskCreationRequestDTO is a Data Transfer Object (DTO) for creating a new task.
 * It contains the necessary fields for a user to create a task in the system.
 */
public class TaskCreationRequestDTO {
    private String title;
    private String description;
    private String longDescription;
    private LocalDate dueDate;
    private TaskPriority priority; 
    private TaskStatus status;
    private String assigneeEmail;
    private Long teamId;
    private Long creatorId;
    private boolean simulateNotificationFailure = false; // This field is used for testing purposes
}