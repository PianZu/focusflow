package de.hsesslingen.focusflowbackend.model;

import de.hsesslingen.focusflowbackend.model.tasks.Task;

import java.util.Set;
import java.util.HashSet;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TeamTest {
    
    private Team team;
    private User user;
    private Task task;

    @BeforeEach
    void setUp() {
        team = new Team();
        user = new User();
        task = new Task();
    }

    @Test
    void testTeamCreation() {
        team.setName("Development Team");
        team.setDescription("Team responsible for development tasks.");
        team.setCreatedAt(LocalDateTime.now());

        assertEquals("Development Team", team.getName());
        assertEquals("Team responsible for development tasks.", team.getDescription());
        assertEquals(team.getCreatedAt().getDayOfMonth(), LocalDateTime.now().getDayOfMonth());
    }

    @Test
    void testTeamMembersRelationship() {
        Set<User> members = new HashSet<>();
        members.add(user);
        team.setMembers(members);

        assertEquals(1, team.getMembers().size());
        assertTrue(team.getMembers().contains(user));
    }

    @Test
    void testTeamTasksRelationship() {
        Set<Task> tasks = new HashSet<>();
        tasks.add(task);
        team.setTasks(tasks);

        assertEquals(1, team.getTasks().size());
        assertTrue(team.getTasks().contains(task));
    }

    @Test
    void testTeamWithNullMembers() {
        team.setMembers(null);
        assertNull(team.getMembers());
    }

    @Test
    void testTeamWithEmptyTasks() {
        team.setTasks(new HashSet<>());
        assertTrue(team.getTasks().isEmpty());
    }
}
