package de.hsesslingen.focusflowbackend;

import de.hsesslingen.focusflowbackend.service.UserService;
import de.hsesslingen.focusflowbackend.model.User;
import de.hsesslingen.focusflowbackend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    // Test: Register user with valid credentials
    @Test
    void testRegisterUser_Valid() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("strongPass123");

        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.registerUser(user);

        assertEquals("test@example.com", result.getEmail());
        assertNotNull(result.getCreatedAt());
    }

    // Test: Register user with invalid password
    @Test
    void testRegisterUser_InvalidPassword() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("123");

        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(user));
        verify(userRepository, never()).save(any());
    }

    // Test: Successful login
    @Test
    void testLogin_Success() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("secret");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        boolean loggedIn = userService.login("test@example.com", "secret");

        assertTrue(loggedIn);
    }

    // Test: Failed login due to wrong password
    @Test
    void testLogin_WrongPassword() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("secret");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        boolean loggedIn = userService.login("test@example.com", "wrong");

        assertFalse(loggedIn);
    }

    // Test: Failed login due to missing user
    @Test
    void testLogin_NoUser() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        boolean loggedIn = userService.login("missing@example.com", "password");

        assertFalse(loggedIn);
    }

    @Test
    void testAssignRole() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setRole("USER");

        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.assignRole(user, "ADMIN");

        assertEquals("ADMIN", result.getRole());
        verify(userRepository).save(user);
}

}

