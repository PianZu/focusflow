package de.hsesslingen.focusflowbackend.service;

import de.hsesslingen.focusflowbackend.model.User;
import de.hsesslingen.focusflowbackend.model.Team;
import de.hsesslingen.focusflowbackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
// Test class for UserService following the Arrange-Act-Assert pattern
// and using Mockito for mocking dependencies.
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private Team testTeam;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setEmail("testuser@example.com");
        testUser.setPassword("Password123!");
        testUser.setRole("USER");

        testTeam = new Team();
        testTeam.setName("Development");
        testTeam.setDescription("Dev team for the project");
    }

    @Test
    public void testRegisterUserValid() {
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User savedUser = userService.registerUser(testUser);

        assertNotNull(savedUser);
        assertEquals("testuser@example.com", savedUser.getEmail());
        assertEquals("USER", savedUser.getRole());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    public void testRegisterUserInvalidPassword() {
        testUser.setPassword("short");

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, 
            () -> userService.registerUser(testUser));

        assertEquals("Invalid user data", thrown.getMessage());
    }

    @Test
    public void testLoginUserCorrectCredentials() {
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        boolean loginSuccess = userService.loginUser(testUser.getEmail(), testUser.getPassword());

        assertTrue(loginSuccess);
        verify(userRepository, times(1)).findByEmail(testUser.getEmail());
        verify(passwordEncoder, times(1)).matches(testUser.getPassword(), testUser.getPassword());
    }

    @Test
    public void testLoginUserIncorrectPassword() {
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        boolean loginSuccess = userService.loginUser(testUser.getEmail(), testUser.getPassword());

        assertFalse(loginSuccess);
        verify(userRepository, times(1)).findByEmail(testUser.getEmail());
        verify(passwordEncoder, times(1)).matches(testUser.getPassword(), testUser.getPassword());
    }

    @Test
    public void testAssignRole() {
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User updatedUser = userService.assignRole(testUser, "ADMIN");

        assertNotNull(updatedUser);
        assertEquals("ADMIN", updatedUser.getRole());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    public void testAddUserToTeam() {
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        userService.addUserToTeam(testUser, testTeam);

        assertTrue(testUser.getTeams().contains(testTeam));
        assertTrue(testTeam.getMembers().contains(testUser));
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    public void testRemoveUserFromTeam() {
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        userService.addUserToTeam(testUser, testTeam); // First, add the user to the team

        userService.removeUserFromTeam(testUser, testTeam);

        assertFalse(testUser.getTeams().contains(testTeam));
        assertFalse(testTeam.getMembers().contains(testUser));
        verify(userRepository, times(2)).save(testUser); // Called twice: once for adding, once for removing
    }
}

