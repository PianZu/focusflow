package de.hsesslingen.focusflowbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import de.hsesslingen.focusflowbackend.model.tasks.Task;

@Entity
@Data
@EqualsAndHashCode(exclude = {"teams", "tasks"})
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @JsonIgnore  // verhindert Rückserialisierung von Team → User → Team
    private Set<Team> teams = new HashSet<>();

    @OneToMany(mappedBy = "assignee", cascade = CascadeType.ALL)
    @JsonIgnore  // verhindert Rückserialisierung von Task → User → Task
    private Set<Task> tasks = new HashSet<>();
}
