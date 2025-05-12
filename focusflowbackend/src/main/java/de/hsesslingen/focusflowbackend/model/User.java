package de.hsesslingen.focusflowbackend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

import de.hsesslingen.focusflowbackend.model.tasks.Task;

@Entity
@Data
@EqualsAndHashCode(exclude = {"teams", "tasks"})
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

    @Transient
    private String passwordConfirm;

    @Column(nullable = false)
    private String role;

    private String firstName;
    private String lastName;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime lastLogin;

    @ManyToMany(mappedBy = "members")
    @JsonBackReference
    private Set<Team> teams = new HashSet<>();

    @OneToMany(mappedBy = "assignee", cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<Task> tasks = new HashSet<>();
}
