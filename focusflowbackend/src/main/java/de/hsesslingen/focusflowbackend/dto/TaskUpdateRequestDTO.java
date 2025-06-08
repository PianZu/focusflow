package de.hsesslingen.focusflowbackend.dto;

import de.hsesslingen.focusflowbackend.model.tasks.TaskPriority;
import de.hsesslingen.focusflowbackend.model.tasks.TaskStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskUpdateRequestDTO {
    private String title;
    private String description;
    private String longDescription;
    private LocalDate dueDate;
    private Long assigneeId;
    private Long teamId;
    private TaskPriority priority;
    private TaskStatus status;
}