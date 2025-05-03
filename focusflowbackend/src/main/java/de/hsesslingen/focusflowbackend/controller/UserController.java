package de.hsesslingen.focusflowbackend.controller;

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
public class UserController {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final UserService userService;

    // GET: Get user by ID
    @GetMapping()
    public ResponseEntity<User> getUserById(@RequestParam Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // POST: Register a new user
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (user.getPasswordConfirm()== null || !user.getPassword().equals(user.getPasswordConfirm())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password confirmation does not match.");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists."); 
        }
        try {
            user.setRole("USER");
            userService.registerUser(user);
    
            // Redirect to /login after successful registration
            return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", "/login")
                .build();
    
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // POST: Login a user
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestParam String email, @RequestParam String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return ResponseEntity.ok("Login successful!");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    // PUT: Update user details
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

    // PUT: Update user role
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

    // POST: Add user to team
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
        teamRepository.save(team);

        return ResponseEntity.ok("User added to team.");
    }

    // DELETE: Remove user from team
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
        teamRepository.save(team);

        return ResponseEntity.ok("User removed from team.");
    }
}

