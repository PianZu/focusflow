package de.hsesslingen.focusflowbackend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import de.hsesslingen.focusflowbackend.model.tasks.Task;

@Entity
@Data
@Table(name = "users")
// This class represents a user in the system
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String firstName;
    private String lastName;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime lastLogin;

    @ManyToMany(mappedBy = "members")
    private Set<Team> teams = new HashSet<>();

    @OneToMany(mappedBy = "assignee", cascade = CascadeType.ALL)
    private Set<Task> tasks = new HashSet<>();

    @Column(nullable = false)
    private String role = "USER"; // New: Default role
}
