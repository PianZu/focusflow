package de.hsesslingen.focusflowbackend.service;

import de.hsesslingen.focusflowbackend.model.Team;
import de.hsesslingen.focusflowbackend.model.User;
import de.hsesslingen.focusflowbackend.repository.TeamRepository;
import de.hsesslingen.focusflowbackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private Team testTeam;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("testuser@example.com");
        testUser.setPassword("Password123!");
        testUser.setPasswordConfirm("Password123!");
        testUser.setRole("USER");
        testUser.setTeams(new HashSet<>());
        testUser.setTasks(new HashSet<>());

        testTeam = new Team();
        testTeam.setId(10L);
        testTeam.setName("Development");
        testTeam.setDescription("Dev team for the project");
        testTeam.setMembers(new HashSet<>());
        testTeam.setTasks(new HashSet<>());
    }

    @Test
    public void testAddUserToTeam() {
        // Arrange
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser)); 
        when(teamRepository.findById(testTeam.getId())).thenReturn(Optional.of(testTeam));

        // Mock the save operation for the user
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        userService.addUserToTeam(testUser, testTeam);

        // Assert
        assertTrue(testUser.getTeams().contains(testTeam), "User's team set should contain the team.");
        assertTrue(testTeam.getMembers().contains(testUser), "Team's member set should contain the user.");
        verify(userRepository, times(1)).findById(testUser.getId()); 
        verify(teamRepository, times(1)).findById(testTeam.getId()); 
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    public void testRemoveUserFromTeam() {
        testUser.getTeams().add(testTeam);
        testTeam.getMembers().add(testUser);

        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        userService.removeUserFromTeam(testUser, testTeam);

        // Assert
        assertFalse(testUser.getTeams().contains(testTeam), "User's team set should not contain the team.");
        assertFalse(testTeam.getMembers().contains(testUser), "Team's member set should not contain the user.");
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    public void testRegisterUserValid() {
        when(passwordEncoder.encode(testUser.getPassword())).thenReturn("hashedPassword123");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User userToSave = invocation.getArgument(0);
            userToSave.setPassword("hashedPassword123"); 
            return userToSave;
        });

        User savedUser = userService.registerUser(testUser);

        assertNotNull(savedUser);
        assertEquals("testuser@example.com", savedUser.getEmail());
        assertEquals("USER", savedUser.getRole());
        assertEquals("hashedPassword123", savedUser.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode("Password123!");
    }

    @Test
    public void testRegisterUserInvalidPasswordPolicy() {
        testUser.setPassword("short");

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
            () -> userService.registerUser(testUser));
        
        assertEquals("Password must be between 10 and 12 characters long", thrown.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}