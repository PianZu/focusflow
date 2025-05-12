package de.hsesslingen.focusflowbackend.service;

import de.hsesslingen.focusflowbackend.dto.TaskCreationRequestDTO;
import de.hsesslingen.focusflowbackend.model.Team;
import de.hsesslingen.focusflowbackend.model.User;
import de.hsesslingen.focusflowbackend.model.tasks.Task;
import de.hsesslingen.focusflowbackend.model.tasks.TaskStatus;
import de.hsesslingen.focusflowbackend.repository.TaskRepository;
import de.hsesslingen.focusflowbackend.repository.TeamRepository;
import de.hsesslingen.focusflowbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
@RequiredArgsConstructor
// This service class is responsible for handling task-related operations
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    
    // Method: Create a new task with basic validations
    public Task createTask(TaskCreationRequestDTO request, User creator) {
        // Basic Validations
        if (request.getTitle() == null || request.getTitle().trim().length() < 3) {
            throw new IllegalArgumentException("Task title must be at least 3 characters long");
        }

        if (request.getDueDate() == null) {
            throw new IllegalArgumentException("Due date is required.");
        }

        if (request.getDueDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Due date cannot be in the past");
        }

        // Assignee and Team Logic
        User assignee = null;
        if (request.getAssigneeEmail() != null && !request.getAssigneeEmail().trim().isEmpty()) {
            assignee = userRepository.findByEmail(request.getAssigneeEmail())
                    .orElseThrow(() -> new NoSuchElementException("Assignee user not found with email: " + request.getAssigneeEmail()));
        }

        Team taskTeam = null;
        if (request.getTeamId() != null) {
            taskTeam = teamRepository.findById(request.getTeamId())
                    .orElseThrow(() -> new NoSuchElementException("Team not found with ID: " + request.getTeamId()));

        } else {
            if (assignee != null && !assignee.equals(creator)) {
                if (!doUsersShareAnyTeam(creator, assignee)) {
                    throw new IllegalArgumentException("Cannot assign task to a user outside your team");
                }
            }
        }

        // Task Entity Creation
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setLongDescription(request.getLongDescription());
        task.setDueDate(request.getDueDate().atStartOfDay());

        task.setPriority(request.getPriority());
        task.setStatus(request.getStatus() != null ? request.getStatus() : TaskStatus.OPEN);
        task.setAssignee(assignee);
        task.setTeam(taskTeam);

        Task savedTask = taskRepository.save(task);
        return savedTask;
    }

    // Method: Get all tasks for a specific user
    public List<Task> getTasksForUser(User user) {
        return taskRepository.findByAssigneeId(user.getId());
    }

    // Method: Get all tasks
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // Helper method: Check if two users share at least one common team
    private boolean doUsersShareAnyTeam(User user1, User user2) {
        if (user1 == null || user2 == null || user1.getTeams() == null || user2.getTeams() == null) {
            return false;
        }
        Set<Team> user1Teams = user1.getTeams();
        return user2.getTeams().stream().anyMatch(user1Teams::contains);
    }
}