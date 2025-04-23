package de.hsesslingen.focusflowbackend.repository;

import de.hsesslingen.focusflowbackend.model.Team;
import de.hsesslingen.focusflowbackend.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest // uses a H2 in-memory database for testing
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    private User user;
    private Team team;

    @BeforeEach
    void setUp() {
        
        user = new User();
        user.setEmail("testuser@example.com");
        user.setPassword("password");
        user.setRole("USER");
        userRepository.save(user);

        team = new Team();
        team.setName("Test Team");
        team.setDescription("A team for testing");
        team.setMembers(new HashSet<>());
        teamRepository.save(team);

        // Associate user with team
        team.getMembers().add(user);
        user.getTeams().add(team);

        teamRepository.save(team);
        userRepository.save(user);
    }

    @Test
    void testFindByEmail() {
        Optional<User> found = userRepository.findByEmail("testuser@example.com");
        assertTrue(found.isPresent());
        assertEquals(user.getEmail(), found.get().getEmail());
    }

    @Test
    void testFindByRole() {
        List<User> users = userRepository.findByRole("USER");
        assertFalse(users.isEmpty());
        assertEquals("USER", users.get(0).getRole());
    }

    @Test
    void testFindByTeams() {
        List<User> users = userRepository.findByTeams(team);
        assertFalse(users.isEmpty());
        assertTrue(users.stream().anyMatch(u -> u.getEmail().equals("testuser@example.com")));
    }
}
