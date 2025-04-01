package de.hsesslingen.focusflowbackend.model.tasks;

import de.hsesslingen.focusflowbackend.model.Team;
import de.hsesslingen.focusflowbackend.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaskTest {
    
    private Task task;
    private User assignee;
    private Team team;
    
    @BeforeEach
    void setUp() {
        task = new Task();
        assignee = new User();
        team = new Team();
    }

    @Test
    void testTaskCreation() {
        task.setTitle("Test Task");
        task.setDescription("This is a test task.");
        task.setLongDescription("This is a long description of the test task.");
        task.setDueDate(LocalDateTime.now().plusDays(5));
        task.setPriority(TaskPriority.HIGH);
        task.setStatus(TaskStatus.IN_REVIEW);
        task.setAssignee(assignee);
        task.setTeam(team);

        assertEquals("Test Task", task.getTitle());
        assertEquals("This is a test task.", task.getDescription());
        assertEquals("This is a long description of the test task.", task.getLongDescription());
        assertEquals(task.getDueDate().getDayOfMonth(), LocalDateTime.now().plusDays(5).getDayOfMonth());
        assertEquals(TaskPriority.HIGH, task.getPriority());
        assertEquals(TaskStatus.IN_REVIEW, task.getStatus());
        assertEquals(assignee, task.getAssignee());
        assertEquals(team, task.getTeam());
    }
}
