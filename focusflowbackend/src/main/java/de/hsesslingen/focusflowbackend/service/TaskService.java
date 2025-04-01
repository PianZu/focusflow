package de.hsesslingen.focusflowbackend.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import de.hsesslingen.focusflowbackend.repository.TaskRepository;
import de.hsesslingen.focusflowbackend.repository.TeamRepository;
import de.hsesslingen.focusflowbackend.repository.UserRepository;
import de.hsesslingen.focusflowbackend.model.User;
import de.hsesslingen.focusflowbackend.model.tasks.Task;
import de.hsesslingen.focusflowbackend.model.tasks.TaskPriority;
import de.hsesslingen.focusflowbackend.model.tasks.TaskStatus;
import de.hsesslingen.focusflowbackend.model.Team;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
// This service class is responsible for handling task-related operations
public class TaskService {

    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private TeamRepository teamRepository;

    // Method: Create and save task to the database
    public Task createTask(Long userId, Optional<Long> teamId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found, cannot assign task"));

        Task task = new Task();
        task.setStatus(TaskStatus.OPEN);
        task.setAssignee(user);

        teamId.ifPresent(id -> {
            Team team = teamRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Team not found"));
            task.setTeam(team);
        });

        return taskRepository.save(task);
    }

    // Method: Find task by ID
    public Optional<Task> findTaskById(Long id) {
        return taskRepository.findById(id);
    }

    //Method: Update task status
    public void updateTaskStatus(Long taskId, TaskStatus status) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setStatus(status);
        taskRepository.save(task);
    }

    // Method: Update task priority
    public void updateTaskPriority(Long taskId, TaskPriority priority) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setPriority(priority);
        taskRepository.save(task);
    }  
}
