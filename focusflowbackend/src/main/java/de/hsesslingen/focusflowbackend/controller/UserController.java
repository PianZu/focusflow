package de.hsesslingen.focusflowbackend.controller;

import de.hsesslingen.focusflowbackend.dto.UserRegistrationRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import de.hsesslingen.focusflowbackend.model.Team;
import de.hsesslingen.focusflowbackend.model.User;
import de.hsesslingen.focusflowbackend.repository.UserRepository;
import de.hsesslingen.focusflowbackend.repository.TeamRepository;
import de.hsesslingen.focusflowbackend.service.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
/**
 * UserController handles HTTP requests related to user management.
 * It provides endpoints for user registration, login, profile updates,
 * and team management.
 */
public class UserController {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final UserService userService;

    // GET: Get user by ID
    @GetMapping()
    public ResponseEntity<User> getUserById(@RequestParam Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // GET: Get user by email
    @GetMapping("/email")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // POST: Register a new user with valid credentials
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequestDTO registrationRequest) {
        // Validate password confirmation
        if (registrationRequest.getPasswordConfirm() == null ||
            !registrationRequest.getPassword().equals(registrationRequest.getPasswordConfirm())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Password confirmation does not match");
        }

        // Check if email already exists
        if (userRepository.findByEmail(registrationRequest.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT) 
                    .body("Email already registered");
        }

        try {
            // Create a new User entity from the DTO
            User newUser = new User();
            newUser.setEmail(registrationRequest.getEmail());
            newUser.setFirstName(registrationRequest.getFirstName());
            newUser.setLastName(registrationRequest.getLastName());
            newUser.setPassword(registrationRequest.getPassword());
            newUser.setRole("USER");

            userService.registerUser(newUser);

            // Successful registration
            return ResponseEntity.ok("Registration successful!");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    // POST: Login user with valid credentials
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestParam String email, @RequestParam String password) {
        boolean success = userService.loginUser(email, password);
        if (success) {
            return ResponseEntity.ok("Login successful!");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    // PUT: Update user profile information
    @PutMapping("/profile")
    public ResponseEntity<User> updateProfile(@RequestParam String email, @RequestBody User updatedInfo) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        User user = optionalUser.get();
        user.setFirstName(updatedInfo.getFirstName());
        user.setLastName(updatedInfo.getLastName());
        user.setEmail(updatedInfo.getEmail());
        userRepository.save(user);

        return ResponseEntity.ok(user);
    }

    // PUT: Update user role (e.g., to ADMIN or USER)
    @PutMapping("/role")
    public ResponseEntity<?> updateRole(@RequestParam Long id, @RequestParam String role) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = optionalUser.get();
        user.setRole(role);
        userRepository.save(user);

        return ResponseEntity.ok("User role updated to: " + role);
    }

    // POST: Add user to a team
    @PostMapping("/teams/add")
    public ResponseEntity<?> addUserToTeam(@RequestParam Long userId, @RequestParam Long teamId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Team> teamOpt = teamRepository.findById(teamId);
        if (userOpt.isEmpty() || teamOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();
        Team team = teamOpt.get();

        user.getTeams().add(team);
        team.getMembers().add(user);

        userRepository.save(user);

        return ResponseEntity.ok("User added to team.");
    }

    // DELETE: Remove user from a team
    @DeleteMapping("/teams/delete")
    public ResponseEntity<?> removeUserFromTeam(@RequestParam Long userId, @RequestParam Long teamId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Team> teamOpt = teamRepository.findById(teamId);
        if (userOpt.isEmpty() || teamOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();
        Team team = teamOpt.get();

        user.getTeams().remove(team);
        team.getMembers().remove(user);

        userRepository.save(user);

        return ResponseEntity.ok("User removed from team.");
    }
}