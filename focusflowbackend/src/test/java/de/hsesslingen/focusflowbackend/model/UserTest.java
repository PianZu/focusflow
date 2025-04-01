package de.hsesslingen.focusflowbackend.model;

import de.hsesslingen.focusflowbackend.model.tasks.Task;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserTest {

    private User user;
    private Team team;
    private Task task;
    
    @BeforeEach
    void setUp() {
        user = new User();
        team = new Team();
        task = new Task();
    }

    @Test
    void testUserCreation() {
        user.setEmail("test@example.com");
        user.setPassword("securePassword");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setCreatedAt(LocalDateTime.now());

        assertEquals("test@example.com", user.getEmail());
        assertEquals("securePassword", user.getPassword());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals(user.getCreatedAt().getDayOfMonth(), LocalDateTime.now().getDayOfMonth());
    }

    @Test
    void testUserTeamRelationShip() {
        Set<Team> teams = new HashSet<>();
        teams.add(team);
        user.setTeams(teams);

        assertEquals(1, user.getTeams().size());
        assertTrue(user.getTeams().contains(team));
    }

    @Test
    void testUserTaskRelationShip() {
        Set<Task> tasks = new HashSet<>();
        tasks.add(task);
        user.setTasks(tasks);

        assertEquals(1, user.getTasks().size());
        assertTrue(user.getTasks().contains(task));
    }
}
