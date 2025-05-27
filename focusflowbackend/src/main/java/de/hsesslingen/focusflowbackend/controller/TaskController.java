package de.hsesslingen.focusflowbackend.controller;

import de.hsesslingen.focusflowbackend.dto.TaskCreationRequestDTO;
import de.hsesslingen.focusflowbackend.model.User;
import de.hsesslingen.focusflowbackend.model.tasks.Task;
import de.hsesslingen.focusflowbackend.repository.TaskRepository;
import de.hsesslingen.focusflowbackend.repository.UserRepository;
import de.hsesslingen.focusflowbackend.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
/**
 * TaskController handles HTTP requests related to task management.
 * It provides endpoints for creating tasks, retrieving tasks for users,
 * and getting all tasks.
 */
public class TaskController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    // POST: Create a new task
    @PostMapping
    public ResponseEntity<Map<String, Object>> createTask(@RequestBody TaskCreationRequestDTO requestDto) {
        Map<String, Object> responseBody = new HashMap<>();
        try {
            User creator = userRepository.findByIdWithTeams(requestDto.getCreatorId())
                    .orElseThrow(() -> new NoSuchElementException("Creator user not found with ID: " + requestDto.getCreatorId()));

            Task createdTask = taskService.createTask(requestDto, creator);
            responseBody.put("taskId", createdTask.getId());
            responseBody.put("message", "Task created successfully");

            if (requestDto.isSimulateNotificationFailure() && createdTask.getAssignee() != null) {
                responseBody.put("warning", "Task created, but notification failed");
                LOGGER.warn("Task {} created, but notification for assignee {} was simulated as failed.",
                        createdTask.getId(), createdTask.getAssignee().getEmail());
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);

        } catch (IllegalArgumentException e) {
            LOGGER.warn("Task creation failed (Bad Request): {}", e.getMessage());
            responseBody.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
        } catch (NoSuchElementException e) {
            LOGGER.warn("Task creation failed (Not Found): {}", e.getMessage());
            responseBody.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        } catch (Exception e) {
            LOGGER.error("Unexpected error during task creation: {}", e.getMessage(), e);
            responseBody.put("error", "An unexpected server error occurred.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }

    // GET: All tasks for a specific user
    @GetMapping("/user")
    public ResponseEntity<List<Task>> getTasksForUser(@RequestParam Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        List<Task> tasks = taskService.getTasksForUser(user);
        return ResponseEntity.ok(tasks);
    }

    // GET: All tasks for all users
    @GetMapping("/all")
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    // GET: Task by ID
    @GetMapping
    public ResponseEntity<Task> getTaskById(@RequestParam Long id) {
        return taskRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}