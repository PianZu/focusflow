package de.hsesslingen.focusflowbackend.controller;

import de.hsesslingen.focusflowbackend.model.User;
import de.hsesslingen.focusflowbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // User Registration Endpoint
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.registerUser(user));
    }

    // User Login Endpoint
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        boolean loggedIn = userService.login(user.getEmail(), user.getPassword());
        return loggedIn ? ResponseEntity.ok("Login successful") : ResponseEntity.status(401).body("Invalid credentials");
    }

    // Get User Profile
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return userService.findUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update User Profile
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userUpdate) {
        return userService.findUserById(id)
                .map(user -> {
                    user.setFirstName(userUpdate.getFirstName());
                    user.setLastName(userUpdate.getLastName());
                    return ResponseEntity.ok(userService.createUser(user));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Role Management
    @PutMapping("/{id}/role")
    public ResponseEntity<User> assignRole(@PathVariable Long id, @RequestBody String role) {
        return userService.findUserById(id)
                .map(user -> ResponseEntity.ok(userService.assignRole(user, role)))
                .orElse(ResponseEntity.notFound().build());
    }
}

